package com.project.gymtopia.common.data.model;

import com.project.gymtopia.trainer.data.entity.Trainer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerSearchResponse {

  private long id;
  private String trainerName;

  public static TrainerSearchResponse from(Trainer trainer){
    return TrainerSearchResponse.builder()
        .id(trainer.getId())
        .trainerName(trainer.getName())
        .build();
  }
}
