package com.project.gymtopia.trainer.data.model;

import com.project.gymtopia.trainer.data.entity.SpecialProgram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecialProgramDto {

  private long id;
  private String programName;
  private int leftRecruitCount;
  private String trainerName;
  private String trainerEmail;
  private SpecialProgramState recruitState;

  public static SpecialProgramDto from(SpecialProgram specialProgram){
    return SpecialProgramDto.builder()
        .id(specialProgram.getId())
        .programName(specialProgram.getProgramName())
        .leftRecruitCount(specialProgram.getRecruitCount())
        .trainerName(specialProgram.getTrainer().getName())
        .trainerEmail(specialProgram.getTrainer().getEmail())
        .recruitState(specialProgram.getState())
        .build();
  }

}
