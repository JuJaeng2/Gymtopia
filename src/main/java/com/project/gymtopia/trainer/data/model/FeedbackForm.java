package com.project.gymtopia.trainer.data.model;

import com.project.gymtopia.common.data.MissionState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackForm {

  private String contents;
  private MissionState state;

}
