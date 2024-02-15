package com.project.gymtopia.common.data.entity;

import com.project.gymtopia.common.data.MissionState;
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
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn
  private Member member;

  @ManyToOne
  @JoinColumn
  private Trainer trainer;

  private String title;

  private String contents;

  @Enumerated(value = EnumType.STRING)
  private MissionState state;

  private LocalDate createDate;

  private LocalDate expirationDate;

}
