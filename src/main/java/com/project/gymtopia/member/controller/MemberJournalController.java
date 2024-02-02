package com.project.gymtopia.member.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.project.gymtopia.common.data.model.ResponseMessage;
import com.project.gymtopia.exception.ImageUploadException;
import com.project.gymtopia.member.data.model.JournalForm;
import com.project.gymtopia.member.data.model.JournalResponse;
import com.project.gymtopia.member.service.MemberJournalService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberJournalController {

  private final AmazonS3 amazonS3;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final MemberJournalService memberJournalService;

  @PostMapping(value = "/journal", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> uploadJournal(
      Authentication authentication,
      @RequestPart(value = "journal", required = false) JournalForm journalForm,
      @Validated @RequestPart(value = "imageFileList", required = false) List<MultipartFile> imageFileList,
      @RequestPart(value = "videoFile", required = false) List<MultipartFile> videoFile
  ) throws IOException {

    if (!isMultipartFileValid(imageFileList, videoFile)) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(ResponseMessage.builder()
              .message("이미지 파일은 최대 2개, 영상 파일은 최대 1개까지만 업로드 가능합니다.")
              .build()
          );
    }

    String email = authentication.getName();
   if(memberJournalService.uploadJournal(journalForm, email, imageFileList, videoFile.get(0))){
     return ResponseEntity.ok(ResponseMessage.builder()
         .message("일지 저장 성공!!")
         .build());
   }

    return ResponseEntity.ok(ResponseMessage.builder()
        .message("일지 저장 실패...!!")
        .build());
  }



  @GetMapping("/journal/{journalId}")
  public ResponseEntity<?> readJournal(Authentication authentication, @PathVariable long journalId){

    String email = authentication.getName();
    JournalResponse journalResponse = memberJournalService.getJournalDetail(journalId, email);

    return ResponseEntity.ok(journalResponse);
  }

  @PutMapping("/journal/{journalId}")
  public ResponseEntity<?> updateJournal(
      Authentication authentication,
      @PathVariable long journalId,
      @RequestPart(value = "journal", required = false) JournalForm journalForm,
      @Validated @RequestPart(value = "imageFileList", required = false) List<MultipartFile> imageFileList,
      @RequestPart(value = "videoFile", required = false) List<MultipartFile> videoFile
  ) throws ImageUploadException {

    if (!isMultipartFileValid(imageFileList, videoFile)) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(ResponseMessage.builder()
              .message("이미지 파일은 최대 2개, 영상 파일은 최대 1개까지만 업로드 가능합니다.")
              .build()
          );
    }

    String email = authentication.getName();

    log.info(email);
    boolean result = memberJournalService.updateJournal(journalForm, email, journalId, imageFileList, videoFile.get(0));

    if (result){
      return ResponseEntity.ok(ResponseMessage.builder().message("일지 업데이트를 완료했습니다.").build());
    }

    return ResponseEntity.ok(ResponseMessage.builder().message("일지 업데이트를 실패했습니다...!").build());
  }

  @DeleteMapping("/journal/{journalId}")
  public ResponseEntity<?> deleteJournal(
      Authentication authentication,
      @PathVariable Long journalId
  ){

    String email = authentication.getName();
    log.info("Controller Email >>> {}", journalId);

    ResponseMessage responseMessage = memberJournalService.deleteJournal(journalId, email);
    return ResponseEntity.ok(responseMessage);
  }

  private boolean isMultipartFileValid(
      List<MultipartFile> imageFileList, List<MultipartFile> videoFile) {
    if (imageFileList.size() > 2 || videoFile.size() > 1) {
      return false;
    }
    return true;
  }

}
