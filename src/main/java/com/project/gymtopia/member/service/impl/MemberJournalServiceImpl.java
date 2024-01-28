package com.project.gymtopia.member.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.gymtopia.common.data.JournalType;
import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.common.data.model.ResponseMessage;
import com.project.gymtopia.common.repository.MissionRepository;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.exception.ImageUploadException;
import com.project.gymtopia.member.data.entity.Journal;
import com.project.gymtopia.member.data.entity.Media;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.ImageUploadState;
import com.project.gymtopia.member.data.model.ImageUrlResponse;
import com.project.gymtopia.member.data.model.JournalForm;
import com.project.gymtopia.member.repository.JournalRepository;
import com.project.gymtopia.member.repository.MediaRepository;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.service.MemberJournalService;
import com.project.gymtopia.util.MultipartUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberJournalServiceImpl implements MemberJournalService {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;
  private final JournalRepository journalRepository;
  private final MediaRepository mediaRepository;
  private final MissionRepository missionRepository;
  private final MemberRepository memberRepository;


  @Override
  public ResponseMessage uploadJournal(JournalForm journalForm, String email)
      throws IOException {
    //TODO: 영상파일의 저장 및 삭제 기능 구현 필요

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    // 받은 미션이 있는지 확인 후 타입 설정
    Optional<Mission> optionalMission = checkMission(member);


    // 저널 내용 저장
    Journal journal = saveJournalDetail(journalForm.getTitle(), journalForm.getContents(),
        optionalMission);

    // S3 이미지 저장

    List<String> imageUrlList = new ArrayList<>();

    for (MultipartFile file : journalForm.getMultipartFileList()) {

      ImageUploadState imageUploadState = uploadImageToS3(file);

      if (!imageUploadState.isSuccess()) {
        for (String imageUrl : imageUrlList) {
          String[] split = imageUrl.split("https://gymtopia.s3.ap-northeast-2.amazonaws.com/");
          String imageName = split[1];
          deleteImage(imageName);
        }
        throw new ImageUploadException(ErrorCode.IMAGE_UPLOAD_FAIL);
      }

      imageUrlList.add(imageUploadState.getImageUrl());
    }

    // 이미지 경로 저장
    for (String imageUrl : imageUrlList) {
      saveImageUrl(journal, imageUrl);
    }

    return ResponseMessage.builder()
        .message("일지 저장 성공!!")
        .build();
  }

  private Journal saveJournalDetail(String title, String contents, Optional<Mission> optionalMission) {

    JournalType journalType = JournalType.MISSION_JOURNAL;
    if (optionalMission.isEmpty()) {
      journalType = JournalType.DAILY_JOURNAL;
    }

    Journal newJournal = Journal.builder()
        .type(journalType)
        .mission(journalType == JournalType.MISSION_JOURNAL ? optionalMission.get() : null)
        .title(title)
        .contents(contents)
        .build();

    return journalRepository.save(newJournal);

  }

  private Optional<Mission> checkMission(Member member) {

    Optional<Mission> optionalMission = missionRepository.findByMember(member);
    return optionalMission;
  }


  /**
   * 일지에 들어갈 이미지를 AWS S3에 저장
   *
   * @param file
   * @return
   */
  private ImageUploadState uploadImageToS3(MultipartFile file) {

    String filename = file.getOriginalFilename();
    String newFilename = MultipartUtil.createFileId(filename);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType("image/jpeg");

    try {
      amazonS3.putObject(
          new PutObjectRequest(bucket, newFilename, file.getInputStream(), metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead));

    } catch (IOException e) {
      e.printStackTrace();
      return ImageUploadState.builder()
          .success(false)
          .build();
    }

    return ImageUploadState.builder()
        .success(true)
        .imageUrl(amazonS3.getUrl(bucket, newFilename).toString())
        .build();
  }

  /**
   * 일지에 들어갈 이미지의 URL을 Media 엔티티에 저장
   */
  private void saveImageUrl(Journal journal, String imageUrl) {

    Media newMedia = Media.builder()
        .url(imageUrl)
        .journal(journal)
        .build();

    mediaRepository.save(newMedia);
  }

  private String deleteImage(String fileName){
    try{
      amazonS3.deleteObject(bucket, fileName);
    }catch (SdkClientException e){
      throw new CustomException(ErrorCode.IMAGE_DELETE_FAIL);
    }

    return "이미지 삭제 완료";
  }

}
