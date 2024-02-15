package com.project.gymtopia.trainer.data.entity;

import com.project.gymtopia.common.data.entity.BaseEntity;
import com.project.gymtopia.common.roles.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trainer extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  @Column(unique = true)
  private String email;
  private String password;
  private String number;
  private LocalDate birth;
  private String gymName;
  private String introduction;
  private String career;

  @Enumerated(EnumType.STRING)
  private Roles role;

  private LocalDate removedDate;

}
