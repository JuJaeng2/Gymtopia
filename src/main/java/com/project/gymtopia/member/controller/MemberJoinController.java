package com.project.gymtopia.member.controller;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserSignInForm;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.member.data.model.MemberDto;
import com.project.gymtopia.member.data.model.MemberResponse;
import com.project.gymtopia.member.data.model.WithdrawForm;
import com.project.gymtopia.member.service.MemberAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member Join Controller", description = "MEMBER 권한을 받는 회원가인, 로그인 컨트롤러")
public class MemberJoinController {

  private final MemberAuthService memberAuthService;

  /**
   * 회원 권한을 가지는 회원 가입
   */
  @Operation(summary = " 회원 권한을 가지는 회원 가입")
  @PostMapping("/signUp/member")
  public ResponseEntity<MemberResponse> memberSignUp(@RequestBody UserSignUpForm userSignUpForm) {

    MemberResponse memberResponse = memberAuthService.signUp(userSignUpForm);

    return ResponseEntity.ok(memberResponse);
  }

  /**
   * 회원 로그인
   */
  @Operation(summary = "회원 로그인")
  @PostMapping("/signIn/member")
  public ResponseEntity<TokenResponse> memberSignIn(@RequestBody UserSignInForm userSignInForm) {

    MemberDto memberDto = memberAuthService.authenticate(userSignInForm.getEmail(),
        userSignInForm.getPassword());

    TokenResponse tokenResponse = memberAuthService.createToken(memberDto);

    return ResponseEntity.ok(tokenResponse);
  }

  @Operation(summary = "회원 계정 탈퇴")
  @PostMapping("/withdraw/member")
  public ResponseEntity<?> memberWithdraw(
      Authentication authentication,
      @RequestBody WithdrawForm withdrawForm){

    memberAuthService.withdraw(authentication.getName(), withdrawForm);
    return ResponseEntity.ok("탈퇴처리가 완료되었습니다.");
  }


}
