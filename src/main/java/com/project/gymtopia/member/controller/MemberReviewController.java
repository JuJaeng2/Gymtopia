package com.project.gymtopia.member.controller;

import com.project.gymtopia.member.data.model.ReviewForm;
import com.project.gymtopia.member.service.MemberReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member Review Controller", description = "회원의 트레이너에 대한 리뷰 과련 컨트롤러")
public class MemberReviewController {

  private final MemberReviewService memberReviewService;

  /**
   * 리뷰 작성 API
   */
  @Operation(summary = "리뷰 작성")
  @PostMapping("/review/trainer/{trainerId}")
  public ResponseEntity<?> writeReview(
      Authentication authentication,
      @PathVariable(value = "trainerId") long trainerId,
      @RequestBody ReviewForm reviewForm
  ){

    String email = authentication.getName();
    memberReviewService.writeReview(email, reviewForm, trainerId);
    return ResponseEntity.ok("리뷰를 작성했습니다.");
  }

  /**
   * 리뷰 수정 API
   */
  @Operation(summary = "리뷰 수정")
  @PutMapping("/review/{reviewId}/trainer/{trainerId}")
  public ResponseEntity<?> updateReview(
      Authentication authentication,
      @PathVariable(value = "reviewId") long reviewId,
      @PathVariable(value = "trainerId") long trainerId,
      @RequestBody ReviewForm reviewForm
  ){
    String email = authentication.getName();
    memberReviewService.updateReview(email, reviewForm, reviewId, trainerId);
    return ResponseEntity.ok("리뷰 수정이 완료되었습니다.");
  }

  /**
   * 리뷰 삭제 API
   */
  @Operation(summary = "리뷰 삭제")
  @DeleteMapping("/review/{reviewId}")
  public ResponseEntity<?> deleteReview(
      Authentication authentication,
      @PathVariable(value = "reviewId") long reviewId
  ){
    String email = authentication.getName();
    memberReviewService.deleteReview(email, reviewId);
    return ResponseEntity.ok("리뷰 삭제가 완료되었습니다.");
  }
}
