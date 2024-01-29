package com.project.gymtopia.member.service.impl;

import static com.project.gymtopia.exception.ErrorCode.JOURNAL_NOT_FOUND;
import static com.project.gymtopia.exception.ErrorCode.NOT_SAME_MEMBER_AND_WRITER;
import static com.project.gymtopia.exception.ErrorCode.USER_NOT_FOUND;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.gymtopia.common.data.JournalType;
import com.project.gymtopia.common.data.MissionState;
import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.common.data.model.ResponseMessage;
import com.project.gymtopia.common.repository.MissionRepository;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.exception.ImageUploadException;
import com.project.gymtopia.member.data.entity.Journal;
import com.project.gymtopia.member.data.entity.Media;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.JournalForm;
import com.project.gymtopia.member.data.model.JournalResponse;
import com.project.gymtopia.member.data.model.MediaResponse;
import com.project.gymtopia.member.data.model.MediaUploadState;
import com.project.gymtopia.member.repository.JournalRepository;
import com.project.gymtopia.member.repository.MediaRepository;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.service.MemberJournalService;
import com.project.gymtopia.trainer.data.entity.FeedBack;
import com.project.gymtopia.trainer.data.model.FeedBackDto;
import com.project.gymtopia.trainer.repository.FeedBackRepository;
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
  private final FeedBackRepository feedBackRepository;


  @Override
  public boolean uploadJournal(
      JournalForm journalForm,
      String email,
      List<MultipartFile> imageFileList,
      MultipartFile videoMultipartFile) throws IOException {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    // 받은 미션이 있는지 확인 후 타입 설정
    Optional<Mission> optionalMission = checkMission(member);

    // 저널 내용 저장
    Journal journal = saveJournalDetail(
        journalForm.getTitle(),
        journalForm.getContents(),
        optionalMission,
        member);

    // S3 이미지 저장
    List<String> mediaFileUrlList = new ArrayList<>();

    MediaUploadState videoUploadState = uploadFileToS3(videoMultipartFile);
    checkUploadState(videoUploadState, mediaFileUrlList);

    for (MultipartFile imageMultipartFile : imageFileList){
      MediaUploadState imageUploadState = uploadFileToS3(imageMultipartFile);
      checkUploadState(imageUploadState, mediaFileUrlList);
    }

    // 파일 저장
    for (String mediaFileUrl : mediaFileUrlList) {
      saveImageUrl(journal, mediaFileUrl);
    }

    //TODO: 일지 저장이 완료된 후 트레이너에게 알림 보내기

    return true;
  }

  private void checkUploadState(MediaUploadState mediaUploadState, List<String> mediaFileUrlList)
      throws ImageUploadException {
    String url = mediaUploadState.getFileUrl();
    if (!mediaUploadState.isSuccess() && !mediaFileUrlList.isEmpty()){
      int idx = url.lastIndexOf("/");
      String fileName = url.substring(idx + 1);
      deleteImage(fileName);

      throw new ImageUploadException(ErrorCode.IMAGE_UPLOAD_FAIL);
    }
    mediaFileUrlList.add(url);
  }

  /**
   * 일지의 구체적인 내용을 반환하는 메소드
   */
  @Override
  public JournalResponse getJournalDetail(long journalId, String email) {

    // 해당 유저가 존재하는지 체크
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    // 저널을 쓴 사람이 요청한 사람과 동일한지 확인하기
    Journal journal = journalRepository.findById(journalId)
        .orElseThrow(() -> new CustomException(JOURNAL_NOT_FOUND));

    if (!journal.getMember().getEmail().equals(member.getEmail())) {
      throw new CustomException(ErrorCode.NOT_SAME_MEMBER_AND_WRITER);
    }

    // JournalResponse 리턴
    MediaResponse mediaResponse;
    Optional<List<Media>> optionalMediaList = mediaRepository.findALlByJournal(journal);

    if (optionalMediaList.isPresent()) {
      mediaResponse = classifyMedia(optionalMediaList.get());
    } else {
      mediaResponse = null;
    }

    FeedBackDto feedBackDto;
    Optional<FeedBack> optionalFeedBack = feedBackRepository.findByJournal(journal);
    if (optionalFeedBack.isPresent()) {
      FeedBack feedBack = optionalFeedBack.get();
      feedBackDto = FeedBackDto.builder()
          .contents(feedBack.getContents())
          .trainerName(feedBack.getTrainer().getName())
          .build();
    } else {
      feedBackDto = null;
    }

    return JournalResponse.builder()
        .title(journal.getTitle())
        .contents(journal.getContents())
        .journalType(journal.getType())
        .mission(journal.getMission())
        .createDateTime(journal.getCreateDateTime())
        .mediaResponse(mediaResponse)
        .feedBackDto(feedBackDto)
        .build();
  }

  /**
   * 일지 업데이트 메소드
   */
  @Override
  public boolean updateJournal(JournalForm journalForm, String email, long journalId, List<MultipartFile> imageMultipartFileList, MultipartFile videoMultipartFile)
      throws ImageUploadException {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    Journal journal = journalRepository.findById(journalId)
        .orElseThrow(() -> new CustomException(JOURNAL_NOT_FOUND));

    // 일지 업데이트 요청자와 일지의 작성자가 동일한지 확인
    if (!journal.getMember().getEmail().equals(member.getEmail())) {
      throw new CustomException(ErrorCode.NOT_SAME_MEMBER_AND_WRITER);
    }

    List<Media> mediaList = mediaRepository.findALlByJournal(journal)
        .orElseThrow(() -> new CustomException(JOURNAL_NOT_FOUND));


    // Journal 업데이트
    journal.setTitle(journalForm.getTitle());
    journal.setContents(journal.getContents());
    journalRepository.save(journal);

    //이미지 업데이트
    //S3에 있는 이미지 전부 삭제
    for (Media media : mediaList) {
      String url = media.getUrl();
      int idx = url.lastIndexOf("/");
      String imageName = url.substring(idx + 1);
      deleteImage(imageName);
    }

    // S3에 파일 저장, Media엔티티 저장
    List<String> mediaFileUrlList = new ArrayList<>();

    if (!videoMultipartFile.isEmpty()){
      MediaUploadState videoUploadState = uploadFileToS3(videoMultipartFile);
      checkUploadState(videoUploadState, mediaFileUrlList);
    }

    if (!imageMultipartFileList.isEmpty() ){
      for (MultipartFile imageMultipartFile : imageMultipartFileList){
        MediaUploadState imageUploadState = uploadFileToS3(imageMultipartFile);
        checkUploadState(imageUploadState, mediaFileUrlList);
      }
    }

    mediaRepository.deleteAll(mediaList);

    // 파일 저장
    for (String mediaFileUrl : mediaFileUrlList) {
      saveImageUrl(journal, mediaFileUrl);
    }

    return true;
  }

  @Override
  public ResponseMessage deleteJournal(long journalId, String email) {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    Journal journal  = journalRepository.findById(journalId)
        .orElseThrow(() -> new CustomException(JOURNAL_NOT_FOUND));

    if (!journal.getMember().getEmail().equals(member.getEmail())){
      throw new CustomException(NOT_SAME_MEMBER_AND_WRITER);
    }

    Optional<List<Media>> optionalMediaList = mediaRepository.findALlByJournal(journal);
    if (optionalMediaList.isPresent()){
      int idx = 0;
      List<Media> mediaList = optionalMediaList.get();
      for (Media media : mediaList){
        idx = media.getUrl().lastIndexOf("/");
        deleteImage(media.getUrl().substring(idx + 1));
      }

      mediaRepository.deleteAll(mediaList);
    }

    journalRepository.delete(journal);

    return ResponseMessage.builder()
        .message("일지 삭제가 완료되었습니다.")
        .build();
  }

  private MediaResponse classifyMedia(List<Media> mediaList) {

    List<String> imageUrlList = new ArrayList<>();
    String videoUrl = null;

    for (Media media : mediaList) {
      String url = media.getUrl();
      int idx = url.lastIndexOf(".");
      String extension = url.substring(idx + 1);
      if (extension.equals("mp4")) {
        videoUrl = url;
        continue;
      }
      imageUrlList.add(url);
    }

    return MediaResponse.builder()
        .imageUrlList(imageUrlList)
        .videoUrl(videoUrl)
        .build();
  }

  /**
   * 일지를 repository에 저장하는 메소드
   */
  private Journal saveJournalDetail(String title, String contents,
      Optional<Mission> optionalMission, Member member) {

    JournalType journalType = optionalMission.isEmpty() ? JournalType.DAILY_JOURNAL : JournalType.MISSION_JOURNAL;

    Journal newJournal = Journal.builder()
        .type(journalType)
        .mission(journalType == JournalType.MISSION_JOURNAL ? optionalMission.get() : null)
        .title(title)
        .contents(contents)
        .member(member)
        .build();

    return journalRepository.save(newJournal);

  }

  /**
   * 회원에게 부여된 미션이 있는지 확인하는 메소드
   */
  private Optional<Mission> checkMission(Member member) {

    Optional<Mission> optionalMission =
        missionRepository.findByMemberAndState(member, MissionState.PROGRESSING);
    return optionalMission;
  }


  /**
   * 일지에 들어갈 이미지를 AWS S3에 저장
   */
  private MediaUploadState uploadFileToS3(MultipartFile multipartFile) {

    String filename = multipartFile.getOriginalFilename();

    // 확장자에 따라 메타데이터의 content-type이 다르게 들어간다.
    int idx = filename.lastIndexOf(".");
    String extension = filename.substring(idx + 1, filename.length());
    String contentType;
    if (extension.equals("mp4")) {
      contentType = "video/mp4";
    } else {
      contentType = "image/" + extension;
    }

    String newFilename = MultipartUtil.createFileId(filename);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(contentType);

    try {
      amazonS3.putObject(
          new PutObjectRequest(bucket, newFilename, multipartFile.getInputStream(), metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead));
    } catch (IOException e) {
      e.printStackTrace();
      return MediaUploadState.builder()
          .success(false)
          .build();
    }

    return MediaUploadState.builder()
        .success(true)
        .fileUrl(amazonS3.getUrl(bucket, newFilename).toString())
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

  /**
   * S3 버킷에 있는 파일을 지워주는 메소드
   */
  private void deleteImage(String fileName) {
    try {
      log.info("MultipartFile Name : {}", fileName);
      amazonS3.deleteObject(bucket, fileName);
    } catch (SdkClientException e) {
      e.printStackTrace();
      throw new CustomException(ErrorCode.IMAGE_DELETE_FAIL);
    }

  }

}
