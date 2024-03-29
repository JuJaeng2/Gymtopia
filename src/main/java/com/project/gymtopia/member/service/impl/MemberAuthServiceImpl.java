package com.project.gymtopia.member.service.impl;

import static com.project.gymtopia.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.project.gymtopia.exception.ErrorCode.WRONG_PASSWORD;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserDto;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.common.roles.Roles;
import com.project.gymtopia.config.jwt.JwtToken;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.data.model.MemberDto;
import com.project.gymtopia.member.data.model.MemberResponse;
import com.project.gymtopia.member.data.model.WithdrawForm;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.member.service.MemberAuthService;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {

  private final MemberRepository memberRepository;
  private final JwtToken jwtToken;
  private final PasswordEncoder passwordEncoder;


  @Override
  public MemberDto authenticate(String email, String password) {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    if (member.getRemovedDate() != null){
      throw new CustomException(ErrorCode.REMOVED_MEMBER_ACCOUNT);
    }

    if (!passwordEncoder.matches(password, member.getPassword())) {
      throw new CustomException(WRONG_PASSWORD);
    }

    return MemberDto.builder()
        .id(member.getId())
        .name(member.getName())
        .email(member.getEmail())
        .role(Roles.MEMBER)
        .build();
  }

  @Override
  public TokenResponse createToken(MemberDto memberDto) {

    return jwtToken.createToken(
        UserDto.builder()
            .name(memberDto.getName())
            .email(memberDto.getEmail())
            .id(memberDto.getId())
            .build(),
        memberDto.getRole());
  }

  @Override
  public MemberResponse signUp(UserSignUpForm userSignUpForm) {

    if (isEmailExist(userSignUpForm.getEmail())) {
      throw new CustomException(ErrorCode.REGISTERED_EMAIL);
    }

    String encodingPassword = passwordEncoder.encode(userSignUpForm.getPassword());

    Member member = Member.builder()
        .name(userSignUpForm.getName())
        .email(userSignUpForm.getEmail())
        .password(encodingPassword)
        .role(Roles.MEMBER)
        .birth(userSignUpForm.getBirth())
        .number(userSignUpForm.getNumber())
        .address(userSignUpForm.getAddress())
        .build();

    Member newMember = memberRepository.save(member);

    return MemberResponse.builder()
        .name(newMember.getName())
        .email(newMember.getEmail())
        .birth(newMember.getBirth())
        .address(newMember.getAddress())
        .address(newMember.getAddress())
        .number(newMember.getNumber())
        .build();
  }

  @Override
  public void withdraw(String email, WithdrawForm withdrawForm) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    if (!member.getEmail().equals(withdrawForm.getEmail())) {
      throw new CustomException(ErrorCode.NOT_SAME_MEMBER);
    }

    member.setRemovedDate(LocalDate.now());

  }

  private boolean isEmailExist(String email) {
    Optional<Member> optionalMember = memberRepository.findByEmail(email);


    //이메일이 있고 탈퇴하지 않은 경우
    return !(optionalMember.isPresent() && optionalMember.get().getRemovedDate() == null);
  }

}
