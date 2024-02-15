package com.project.gymtopia.trainer.data.entity;

import com.project.gymtopia.common.data.entity.BaseEntity;
import com.project.gymtopia.trainer.data.model.SpecialProgramState;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SpecialProgram extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String programName;

  @Enumerated(EnumType.STRING)
  private SpecialProgramState state;

  private int recruitCount;

  @JoinColumn
  @ManyToOne
  private Trainer trainer;

  @ElementCollection
  private List<Long> memberList = new ArrayList<>();


  private LocalDate deadlineDate;

}
