package com.project.gymtopia.member.controller;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserSignInForm;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.member.data.model.MemberDto;
import com.project.gymtopia.member.data.model.MemberResponse;
import com.project.gymtopia.member.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberJoinController {

  private final MemberAuthService memberAuthService;

  /**
   * 회원 권한을 가지는 회원 가입
   */
  @PostMapping("/signUp")
  public ResponseEntity<MemberResponse> memberSignUp(@RequestBody UserSignUpForm userSignUpForm) {

    MemberResponse memberResponse = memberAuthService.signUp(userSignUpForm);

    return ResponseEntity.ok(memberResponse);
  }

  /**
   * 회원 로그인
   */
  @PostMapping("/signIn")
  public ResponseEntity<TokenResponse> memberSignIn(@RequestBody UserSignInForm userSignInForm) {

    MemberDto memberDto = memberAuthService.authenticate(userSignInForm.getEmail(),
        userSignInForm.getPassword());

    TokenResponse tokenResponse = memberAuthService.createToken(memberDto);

    return ResponseEntity.ok(tokenResponse);
  }


}
