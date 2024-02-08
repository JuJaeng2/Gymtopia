package com.project.gymtopia.trainer.service;

import com.project.gymtopia.trainer.data.model.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrainerReviewService {

  Page<ReviewResponse> getAllReview(String email, Pageable pageable);

  void deleteReview(String email, long reviewId);

}
