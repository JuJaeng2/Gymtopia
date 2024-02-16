package com.project.gymtopia.common.data.model;

import com.project.gymtopia.common.data.AlarmType;
import com.project.gymtopia.member.data.model.MemberDto;
import com.project.gymtopia.trainer.data.model.TrainerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class MessageDto {

  private String emitterId;
  private AlarmType alarmType;
  private String from;
  private String message;
  private MemberDto memberDto;
  private TrainerDto trainerDto;


}
