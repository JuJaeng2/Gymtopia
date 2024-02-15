package com.project.gymtopia.trainer.data.model;


import com.project.gymtopia.trainer.data.entity.Management;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagementDto {

  private String memberName;
  private String email;
  private String number;
  private LocalDate birth;
  private String address;
  private LocalDate registerDate;
  private LocalDate endDate;

  public static ManagementDto from(Management management){
    return ManagementDto.builder()
        .memberName(management.getMember().getName())
        .email(management.getMember().getEmail())
        .number(management.getMember().getNumber())
        .address(management.getMember().getAddress())
        .birth(management.getMember().getBirth())
        .registerDate(management.getRegisterDate())
        .endDate(management.getEndDate())
        .build();
  }

}
