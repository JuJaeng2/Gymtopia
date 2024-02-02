package com.project.gymtopia.trainer.data.entity;

import com.project.gymtopia.member.data.entity.Journal;
import jakarta.persistence.Entity;
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
public class FeedBack {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String contents;

  @ManyToOne
  @JoinColumn
  private Trainer trainer;

  @OneToOne
  @JoinColumn
  private Journal journal;

}
