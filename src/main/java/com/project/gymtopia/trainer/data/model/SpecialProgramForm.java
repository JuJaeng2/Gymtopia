package com.project.gymtopia.trainer.data.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpecialProgramForm {

  private String programName;
  private int recruitCount;
  private LocalDate deadlineDate;

}
