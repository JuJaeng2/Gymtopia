package com.project.gymtopia.member.service;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.member.data.model.MemberDto;
import com.project.gymtopia.member.data.model.MemberResponse;
import com.project.gymtopia.member.data.model.WithdrawForm;

public interface MemberAuthService {

  MemberDto authenticate(String email, String password);

  TokenResponse createToken(MemberDto memberDto);

  MemberResponse signUp(UserSignUpForm userSignUpForm);

  void withdraw(String email, WithdrawForm withdrawForm);

}
