package com.project.gymtopia.trainer.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionForm {

  private String title;
  private String contents;

  /*
  1개월 단위로 선택 가능
  ex) period = 1개월 OR period = 5개월
   */
  private int period;

}
