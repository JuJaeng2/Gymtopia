package com.project.gymtopia.trainer.data.model;

import com.project.gymtopia.common.roles.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerDto {

  private Long id;
  private String name;
  private String email;
  private Roles role;

}
