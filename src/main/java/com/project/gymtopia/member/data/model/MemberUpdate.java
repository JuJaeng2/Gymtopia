package com.project.gymtopia.member.data.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdate {

  private String address;
  private String number;
  private LocalDate birth;
  private String password;

}
