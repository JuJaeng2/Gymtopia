package com.project.gymtopia.trainer.data.model;

import com.project.gymtopia.member.data.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {

  private long reviewId;
  private String contents;
  private String writerName;

  public static ReviewResponse from(Review review){
    return ReviewResponse.builder()
        .reviewId(review.getId())
        .contents(review.getContents())
        .writerName(review.getMember().getName())
        .build();
  }
}
