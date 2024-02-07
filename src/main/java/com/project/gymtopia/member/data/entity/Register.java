package com.project.gymtopia.member.data.entity;

import com.project.gymtopia.trainer.data.entity.Trainer;
import jakarta.persistence.Entity;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class Register {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn
  private Member member;

  @ManyToOne
  @JoinColumn
  private Trainer trainer;

  private boolean acceptYn;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate registerDate;
  private LocalDate acceptedDate;
}
