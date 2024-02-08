package com.project.gymtopia.member.service;

import com.project.gymtopia.member.data.model.ReviewForm;

public interface MemberReviewService {

  void writeReview(String email, ReviewForm reviewForm, long trainerId);

  void updateReview(String email, ReviewForm reviewForm, long reviewId, long trainerId);

  void deleteReview(String email, long reviewId);
}
