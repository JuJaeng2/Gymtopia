package com.project.gymtopia.member.service.impl;

import static com.project.gymtopia.exception.ErrorCode.USER_NOT_FOUND;

import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.MemberResponse;
import com.project.gymtopia.member.data.model.MemberUpdate;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.service.MemberInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public MemberResponse getMemberInformation(String email) {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    return MemberResponse.builder()
        .name(member.getName())
        .email(member.getEmail())
        .birth(member.getBirth())
        .address(member.getAddress())
        .number(member.getNumber())
        .build();
  }

  @Override
  public MemberResponse updateInfo(MemberUpdate memberUpdate, String email) {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    member.setNumber(memberUpdate.getNumber());
    member.setPassword(passwordEncoder.encode(memberUpdate.getPassword()));
    member.setBirth(memberUpdate.getBirth());
    member.setAddress(memberUpdate.getAddress());

    Member updatedmember = memberRepository.save(member);

    return MemberResponse.builder()
        .name(updatedmember.getName())
        .email(updatedmember.getEmail())
        .birth(updatedmember.getBirth())
        .number(updatedmember.getNumber())
        .address(updatedmember.getAddress())
        .build();
  }
}
