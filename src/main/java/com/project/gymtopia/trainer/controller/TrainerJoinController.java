package com.project.gymtopia.trainer.controller;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserSignInForm;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.member.data.model.WithdrawForm;
import com.project.gymtopia.trainer.data.model.TrainerDto;
import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.service.TrainerAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Trainer SignUp-SignIn Controller")
public class TrainerJoinController {

  private final TrainerAuthService trainerAuthService;

  @Operation(summary = "트레이너 회원가입")
  @PostMapping("/signUp/trainer")
  public ResponseEntity<TrainerResponse> memberSignUp(@RequestBody UserSignUpForm userSignUpForm) {

    TrainerResponse trainerResponse = trainerAuthService.signUp(userSignUpForm);

    return ResponseEntity.ok(trainerResponse);
  }

  @Operation(summary = "트레이너 로그인")
  @PostMapping("/signIn/trainer")
  public ResponseEntity<TokenResponse> memberSignIn(@RequestBody UserSignInForm userSignInForm) {

    TrainerDto trainerDto = trainerAuthService.authenticate(userSignInForm.getEmail(),
        userSignInForm.getPassword());

    // 톤큰 생성 후 발행
    TokenResponse tokenResponse = trainerAuthService.createToken(trainerDto);

    return ResponseEntity.ok(tokenResponse);
  }

  @Operation(summary = "트레이너 계정 탈퇴")
  @PostMapping("/withdraw/trainer")
  public ResponseEntity<?> memberWithdraw(
      Authentication authentication,
      @RequestBody WithdrawForm withdrawForm){

    trainerAuthService.withdraw(authentication.getName(), withdrawForm);
    return ResponseEntity.ok("탈퇴처리가 완료되었습니다.");
  }


}
