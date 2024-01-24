package com.project.gymtopia.common.service;

import static com.project.gymtopia.exception.ErrorCode.USER_NOT_FOUND;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return memberRepository.findByName(username)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

//  public UserDto authentication(String email, String password) {
//
//  }

}
