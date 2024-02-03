package com.project.gymtopia.trainer.data.model;

import com.project.gymtopia.trainer.data.entity.FeedBack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedBackDto {

  private String trainerName;
  private String contents;

  public static FeedBackDto from(FeedBack feedBack){
    return FeedBackDto.builder()
        .trainerName(feedBack.getTrainer().getName())
        .contents(feedBack.getContents())
        .build();
  }
}
