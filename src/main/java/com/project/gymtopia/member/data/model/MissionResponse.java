package com.project.gymtopia.member.data.model;

import com.project.gymtopia.common.data.MissionState;
import com.project.gymtopia.common.data.entity.Mission;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MissionResponse {

  private String title;
  private String contents;
  private String trainerName;
  private MissionState missionState;
  private LocalDate startDate;
  private LocalDate expirationDate;

  public static MissionResponse from(Mission mission){
    return MissionResponse.builder()
        .title(mission.getTitle())
        .contents(mission.getContents())
        .trainerName(mission.getTrainer().getName())
        .missionState(mission.getState())
        .startDate(mission.getCreateDate())
        .expirationDate(mission.getExpirationDate())
        .build();
  }

}
