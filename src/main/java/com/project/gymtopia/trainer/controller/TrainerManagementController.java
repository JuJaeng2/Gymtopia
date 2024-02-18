package com.project.gymtopia.trainer.controller;

import com.project.gymtopia.member.data.model.JournalResponse;
import com.project.gymtopia.trainer.data.model.FeedbackForm;
import com.project.gymtopia.trainer.data.model.JournalList;
import com.project.gymtopia.trainer.data.model.MemberListResponse;
import com.project.gymtopia.trainer.data.model.MissionForm;
import com.project.gymtopia.trainer.data.model.RegisterManagement;
import com.project.gymtopia.trainer.service.TrainerManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
@Tag(name = "Trainer Management Controller", description = "트레어너의 회원 관리 관련 컨트톨러")
public class TrainerManagementController {

  private final TrainerManagementService trainerManagementService;

  /**
   * 트레이너가 관리중인 회원의 정보를 가져오는 API
   */
  @Operation(summary = "트레이너가 관리중인 회원의 정보 저회")
  @GetMapping("/management/member")
  public ResponseEntity<?> getMemberInfo(
      Authentication authentication
  ){

    String email = authentication.getName();
    MemberListResponse memberListResponse  = trainerManagementService.getMemberInfo(email);
    return ResponseEntity.ok(memberListResponse);
  }

  /**
   * 트레이너가 관리중인 회원 중 특정회원의  모든 일지를 조회하는 API
   * - 회원이 작성한 일지의 제목과 일지의 ID값만 제공
   */
  @Operation(summary = "관리중인 회원 중 특정 회원의 모든 일지 조회")
  @GetMapping("/management/member/{memberId}/journal")
  public ResponseEntity<?> getAllJournalOfOneMember(
      Authentication authentication,
      @PathVariable(value = "memberId") long memberId
  ){

    String email = authentication.getName();
    JournalList journalList  = trainerManagementService.getMissionJournal(email, memberId);

    return ResponseEntity.ok(journalList);
  }

  /**
   * 트레이너가 관리중인 회원 중 특정회원의  특정일지를 조회하는 API
   * - 특정 일지 디테일한 정보 제공
   */
  @Operation(summary = "트레이너가 관리중인 회원 중 특정 회원의  특정 일지를 조회")
  @GetMapping("/management/member/{memberId}/journal/{journalId}")
  public ResponseEntity<?> getJournalOfOneMember(
      Authentication authentication,
      @PathVariable(value = "memberId") long memberId,
      @PathVariable(value = "journalId") long journalId
  ){

    String email = authentication.getName();
    JournalResponse journalResponse  = trainerManagementService.getJournalInfo(email, memberId, journalId);

    return ResponseEntity.ok(journalResponse);
  }

  /**
   * 회원의 일지에대한 피드백을 작성하는 API
   */
  @Operation(summary = "회원의 일지에대한 피드백을 작성")
  @PostMapping("management/feedback/journal/{journalId}")
  public ResponseEntity<?> feedback(
      Authentication authentication,
      @PathVariable(value = "journalId") long journalId,
      @RequestBody FeedbackForm feedBackForm
  ){

    String email = authentication.getName();
    trainerManagementService.writeFeedback(feedBackForm, email, journalId);

    return ResponseEntity.ok("피드백 작성이 완료되었습니다.");
  }

  /**
   * 작성한 피드백을 수정하는 API
   */
  @Operation(summary = " 작성한 피드백을 수정")
  @PutMapping("/management/feedback/journal/{journalId}")
  public ResponseEntity<?> updateFeedback(
      Authentication authentication,
      @PathVariable(value = "journalId") long journalId,
      @RequestBody FeedbackForm feedbackForm
  ){

    String email = authentication.getName();
//    boolean updateFeedback = trainerManagementService.updateFeedback(feedbackForm, email, journalId);

    trainerManagementService.updateFeedback(feedbackForm, email, journalId);

    return ResponseEntity.ok("피드백을 수정하였습니다.");
  }

  /**
   * 작성한 피드백을 삭제하는 API
   */
  @Operation(summary = "작성한 피드백을 삭제")
  @DeleteMapping("/management/feedback/journal/{journalId}")
  public ResponseEntity<?> deleteFeedback(
      Authentication authentication,
      @PathVariable(value = "journalId") long journalId
  ){
    String email = authentication.getName();
    boolean successDelete = trainerManagementService.deleteFeedback(email, journalId);

    if (successDelete){
      return ResponseEntity.ok("피드백이 삭제되었습니다.");
    }else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("피드백을 삭제할 수 없습니다.");
    }
  }

  /**
   * 회원에게 미션을 부여하는 API
   */
  @Operation(summary = "회원에게 미션을 부여")
  @PostMapping("/management/mission/member/{memberId}")
  public ResponseEntity<?> giveMission(
      Authentication authentication,
      @PathVariable(value = "memberId") long memberId,
      @RequestBody MissionForm missionForm
  ){
    String email = authentication.getName();
    trainerManagementService.giveMission(missionForm, email, memberId);

    return ResponseEntity.ok("미션을 성공적으로 전달했습니다.");

  }

  /**
   * 받은 운동관리 신청을 처리하는 API
   */
  @Operation(summary = "받은 운동관리 신청을 처리")
  @PostMapping("/management/register/{registerId}")
  public ResponseEntity<?> manageRegister(
      Authentication authentication,
      @PathVariable(value = "registerId") long registerId,
      @RequestBody RegisterManagement registerManagement
  ){
    String email = authentication.getName();
    boolean isAccepted = trainerManagementService.manageRegister(email, registerId, registerManagement);
    if (isAccepted) {
      return ResponseEntity.ok("요청을 '수락'처리 하였습니다.");
    }else {
      return ResponseEntity.ok("요청을 '거절'처리 하였습니다");
    }

  }

}
