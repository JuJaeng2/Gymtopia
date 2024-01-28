package com.project.gymtopia.member.data.entity;

import com.project.gymtopia.common.data.JournalType;
import com.project.gymtopia.common.data.entity.BaseEntity;
import com.project.gymtopia.common.data.entity.Mission;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Journal extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String title;

  @ManyToOne
  @JoinColumn
  private Member member;

  @OneToOne
  @JoinColumn
  private Mission mission;

  @Enumerated(EnumType.STRING)
  private JournalType type;

  private String contents;

}
