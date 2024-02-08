package com.project.gymtopia.trainer.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerProfile {

  private String trainerName;
  private String email;
  private String number;
  private String gymName;
  private String introduction;
  private String career;
}
