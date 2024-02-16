package com.project.gymtopia.trainer.data.model;

import com.project.gymtopia.common.roles.Roles;
import com.project.gymtopia.trainer.data.entity.Trainer;
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

  public static TrainerDto from(Trainer trainer){
    return TrainerDto.builder()
        .id(trainer.getId())
        .name(trainer.getName())
        .email(trainer.getEmail())
        .role(trainer.getRole())
        .build();
  }

}
