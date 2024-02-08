package com.project.gymtopia.common.data.model;

import com.project.gymtopia.common.data.AlarmType;
import com.project.gymtopia.common.data.entity.Alarm;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmResponse {

  private String contents;
  private String memberName;
  private String trainerName;
  private AlarmType alarmType;
  private LocalDateTime createdDateTime;

  public static AlarmResponse from(Alarm alarm){
    return AlarmResponse.builder()
        .contents(alarm.getContents())
        .memberName(alarm.getMember().getName())
        .trainerName(alarm.getTrainer().getName())
        .alarmType(alarm.getAlarmType())
        .createdDateTime(alarm.getCreateDateTime())
        .build();
  }

}
