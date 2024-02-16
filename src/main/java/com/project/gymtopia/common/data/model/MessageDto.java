package com.project.gymtopia.common.data.model;

import com.project.gymtopia.common.data.AlarmType;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.trainer.data.entity.Trainer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageDto {

  private String emitterId;
  private AlarmType alarmType;
  private String from;
  private String message;
  private Member member;
  private Trainer trainer;

}
