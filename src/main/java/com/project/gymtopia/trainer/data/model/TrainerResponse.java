package com.project.gymtopia.trainer.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerResponse {

  private String name;
  private String email;
  private String number;

}
