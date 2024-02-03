package com.project.gymtopia.trainer.data.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class MemberListResponse {

  private String trainerName;
  private List<ManagementDto> managementList;

}
