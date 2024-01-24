package com.project.gymtopia.member.data.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

  private String name;
  private String email;
  private String address;
  private String number;
  private LocalDate birth;

}
