package com.project.gymtopia.trainer.data.model;


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



}
