package com.project.gymtopia.common.data.entity;

import com.project.gymtopia.common.data.AlarmType;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.trainer.data.entity.Trainer;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String contents;

  @ManyToOne
  @JoinColumn
  private Member member;

  @ManyToOne
  @JoinColumn
  private Trainer trainer;

  @Enumerated(value = EnumType.STRING)
  private AlarmType alarmType;
  private LocalDateTime createDateTime;
}
