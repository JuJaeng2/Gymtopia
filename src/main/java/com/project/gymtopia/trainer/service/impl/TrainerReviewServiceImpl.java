package com.project.gymtopia.trainer.service.impl;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Review;
import com.project.gymtopia.member.repository.ReviewRepository;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.data.model.ReviewResponse;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import com.project.gymtopia.trainer.service.TrainerReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerReviewServiceImpl implements TrainerReviewService {

  private final ReviewRepository reviewRepository;
  private final TrainerRepository trainerRepository;

  @Override
  public Page<ReviewResponse> getAllReview(String email, Pageable pageable) {
    //TODO: 페이징 처리 최적화 필요
    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));
    Page<Review> reviewPage = reviewRepository.findAllByTrainer(trainer, pageable);

    return reviewPage.map(review -> ReviewResponse.from(review));
  }

  @Override
  public void deleteReview(String email, long reviewId) {
    Trainer trainer = trainerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

    if (!review.getTrainer().equals(trainer)){
      throw new CustomException(ErrorCode.INVALID_REVIEW);
    }

    reviewRepository.delete(review);
  }
}
