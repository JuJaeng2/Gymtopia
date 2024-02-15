package com.project.gymtopia.common.service.impl;

import static com.project.gymtopia.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.MemberSecurityDto;
import com.project.gymtopia.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    return MemberSecurityDto.builder()
        .name(member.getName())
        .role(List.of(String.valueOf(member.getRole())))
        .email(member.getEmail())
        .build();
  }


}
