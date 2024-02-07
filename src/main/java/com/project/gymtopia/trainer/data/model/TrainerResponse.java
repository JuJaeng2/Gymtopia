package com.project.gymtopia.trainer.data.model;

import java.time.LocalDate;
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
  private LocalDate birth;
  private String gymName;
  private String introduction;
  private String career;

}
