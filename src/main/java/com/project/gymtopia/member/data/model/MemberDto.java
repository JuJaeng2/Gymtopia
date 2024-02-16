package com.project.gymtopia.member.data.model;

import com.project.gymtopia.common.roles.Roles;
import com.project.gymtopia.member.data.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDto {

  private Long id;
  private String name;
  private String email;
  private Roles role;

  public static MemberDto from(Member member){
    return MemberDto.builder()
        .id(member.getId())
        .name(member.getName())
        .email(member.getEmail())
        .role(member.getRole())
        .build();
  }

}
