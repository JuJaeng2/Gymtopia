package com.project.gymtopia.trainer.controller;

import com.project.gymtopia.trainer.data.model.ReviewResponse;
import com.project.gymtopia.trainer.service.TrainerReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainer")
@Tag(name = "Trainer Review Controller", description = "트레이너 자신의 리뷰를 관리하는 컨트롤러")
public class TrainerReviewController {

  private final TrainerReviewService trainerReviewService;

  /**
   * 트레이너의 모든 리뷰를 확인할 수 있는 API
   */
  @Operation(summary = "트레이너 자신의 모든 리뷰를 확인")
  @GetMapping("/review")
  public ResponseEntity<?> getAllReview(
      Authentication authentication,
      Pageable pageable
  ) {
    String email = authentication.getName();
    Page<ReviewResponse> reviewPage = trainerReviewService.getAllReview(email, pageable);
    return ResponseEntity.ok(reviewPage);
  }

  /**
   * 특정 리뷰 삭제 가능
   */
  @Operation(summary = "트레이너 자신의 특정 리뷰 삭제")
  @DeleteMapping("/review/{reviewId}")
  public ResponseEntity<?> deleteReview(
      Authentication authentication,
      @PathVariable(value = "reviewId") long reviewId
  ) {
    String email = authentication.getName();
    trainerReviewService.deleteReview(email, reviewId);

    return ResponseEntity.ok("리뷰 삭제를 완료하였습니다.");
  }

}
