package com.project.gymtopia.member.controller;

import com.project.gymtopia.common.data.model.ResponseMessage;
import com.project.gymtopia.config.validator.ValidFile;
import com.project.gymtopia.exception.ImageUploadException;
import com.project.gymtopia.member.data.model.JournalForm;
import com.project.gymtopia.member.data.model.JournalResponse;
import com.project.gymtopia.member.service.MemberJournalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
@Tag(name = "Member Journal Controller", description = "회원 일지 관련 컨트롤러")
public class MemberJournalController {

  private final MemberJournalService memberJournalService;

  /**
   * 선택한 미션이 없는 경우 일지를 작성 하는 API
   */
  @Operation(summary = "미션에 대한 일지가 아닌 DAILY_JOURNAL을 작성")
  @PostMapping(value = "/journal", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> uploadJournal(
      Authentication authentication,
      @RequestPart(value = "journal", required = false) JournalForm journalForm,
      @Valid @ValidFile(value = 2) @RequestPart(value = "imageFileList", required = false) List<MultipartFile> imageFileList,
      @Valid @ValidFile(value = 1) @RequestPart(value = "videoFile", required = false) List<MultipartFile> videoFile
  ) throws IOException {

    String email = authentication.getName();
    if (memberJournalService.uploadJournal(journalForm, email, imageFileList, videoFile.get(0))) {
      return ResponseEntity.ok(ResponseMessage.builder()
          .message("일지 저장 성공!!")
          .build());
    }

    return ResponseEntity.ok(ResponseMessage.builder()
        .message("일지 저장 실패...!!")
        .build());
  }

  /**
   * 미션을 선택하고 미션에 관한 일지를 작성 하는 API
   */
  @Operation(summary = "미션을 선택하고 미션에 관한 일지(MISSION_JOURNAL)을 작성")
  @PostMapping(value = "/journal/mission/{missionId}"
      , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> uploadJournalWithMission(
      Authentication authentication,
      @PathVariable(value = "missionId") long missionId,
      @RequestPart(value = "journal", required = false) JournalForm journalForm,
      @Valid @ValidFile(value = 2) @RequestPart(value = "imageFileList", required = false) List<MultipartFile> imageFileList,
      @Valid @ValidFile(value = 1) @RequestPart(value = "videoFile", required = false) List<MultipartFile> videoFile
  ) throws IOException {

    String email = authentication.getName();
    if (memberJournalService.uploadJournal(journalForm, email, missionId, imageFileList,
        videoFile.get(0))) {
      return ResponseEntity.ok(ResponseMessage.builder()
          .message("일지 저장 성공!!")
          .build());
    }

    return ResponseEntity.ok(ResponseMessage.builder()
        .message("일지 저장 실패...!!")
        .build());
  }

  /**
   * 특정 일지를 읽기 위한 API
   */
  @Operation(summary = "특정 일지정보 조회")
  @GetMapping("/journal/{journalId}")
  public ResponseEntity<?> readJournal(Authentication authentication,
      @PathVariable long journalId) {

    String email = authentication.getName();
    JournalResponse journalResponse = memberJournalService.getJournalDetail(journalId, email);

    return ResponseEntity.ok(journalResponse);
  }

  /**
   * 일지를 업데이트 하기 위한 API
   */
  @Operation(summary = "특정 일지 업데이트")
  @PutMapping("/journal/{journalId}")
  public ResponseEntity<?> updateJournal(
      Authentication authentication,
      @PathVariable long journalId,
      @RequestPart(value = "journal", required = false) JournalForm journalForm,
      @Valid @ValidFile(value = 2) @RequestPart(value = "imageFileList", required = false) List<MultipartFile> imageFileList,
      @Valid @ValidFile(value = 1) @RequestPart(value = "videoFile", required = false) List<MultipartFile> videoFile
  ) throws ImageUploadException {

    String email = authentication.getName();

    log.info(email);
    boolean result = memberJournalService.updateJournal(journalForm, email, journalId,
        imageFileList, videoFile.get(0));

    if (result) {
      return ResponseEntity.ok(ResponseMessage.builder().message("일지 업데이트를 완료했습니다.").build());
    }

    return ResponseEntity.ok(ResponseMessage.builder().message("일지 업데이트를 실패했습니다...!").build());
  }

  /**
   * 일지를 삭제하기 위한 API
   */
  @Operation(summary = "특정 일지 삭제")
  @DeleteMapping("/journal/{journalId}")
  public ResponseEntity<?> deleteJournal(
      Authentication authentication,
      @PathVariable Long journalId
  ) {

    String email = authentication.getName();
    log.info("Controller Email >>> {}", journalId);

    ResponseMessage responseMessage = memberJournalService.deleteJournal(journalId, email);
    return ResponseEntity.ok(responseMessage);
  }

  private boolean isMultipartFileValid(
      List<MultipartFile> imageFileList, List<MultipartFile> videoFile) {
    if (!imageFileList.isEmpty() && (imageFileList.size() > 2 || videoFile.size() > 1)) {
      return false;
    }
    return true;
  }

}
