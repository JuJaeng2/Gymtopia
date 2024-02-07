package com.project.gymtopia.member.service.impl;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.entity.Review;
import com.project.gymtopia.member.data.model.ReviewForm;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.repository.ReviewRepository;
import com.project.gymtopia.member.service.MemberReviewService;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.repository.ManagementRepository;
import com.project.gymtopia.trainer.repository.TrainerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberReviewServiceImpl implements MemberReviewService {

  private final ReviewRepository reviewRepository;
  private final MemberRepository memberRepository;
  private final TrainerRepository trainerRepository;
  private final ManagementRepository managementRepository;

  @Override
  public void writeReview(String email, ReviewForm reviewForm, long trainerId) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Trainer trainer = trainerRepository.findById(trainerId)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    managementRepository.findByTrainerAndMember(trainer, member);

    Optional<Review> optionalReview = reviewRepository.findByMemberAndTrainer(member, trainer);
    if (optionalReview.isPresent()){
      throw new CustomException(ErrorCode.REVIEW_ALREADY_EXIST);
    }

    Review newReview = Review.builder()
        .contents(reviewForm.getContents())
        .member(member)
        .trainer(trainer)
        .build();

    reviewRepository.save(newReview);
  }

  @Override
  public void updateReview(String email, ReviewForm reviewForm, long reviewId, long trainerId) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Trainer trainer = trainerRepository.findById(trainerId)
        .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));

    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

    if (!review.getMember().equals(member)){
      throw new CustomException(ErrorCode.NOT_WRITER_OF_REVIEW);
    }

    if (!review.getTrainer().equals(trainer)){
      throw new CustomException(ErrorCode.NOT_SAME_TRAINER);
    }


    review.setContents(reviewForm.getContents());

    reviewRepository.save(review);

  }

  @Override
  public void deleteReview(String email, long reviewId) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

    if (!review.getMember().equals(member)){
      throw new CustomException(ErrorCode.NOT_WRITER_OF_REVIEW);
    }

    reviewRepository.delete(review);
  }
}
