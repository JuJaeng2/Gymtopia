package com.project.gymtopia.common.data.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpForm {

  private String name;
  private String password;
  private String email;
  private String number;
  private LocalDate birth;
  private String address;

}
