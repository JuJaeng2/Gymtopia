package com.project.gymtopia.member.data.model;

import com.project.gymtopia.common.data.MissionState;
import java.time.LocalDateTime;
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
  private LocalDateTime startDateTime;
  private LocalDateTime expirationDateTime;

}
