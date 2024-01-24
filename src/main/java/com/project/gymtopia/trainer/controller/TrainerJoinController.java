package com.project.gymtopia.trainer.controller;

import com.project.gymtopia.common.data.model.TokenResponse;
import com.project.gymtopia.common.data.model.UserSignInForm;
import com.project.gymtopia.common.data.model.UserSignUpForm;
import com.project.gymtopia.trainer.data.model.TrainerDto;
import com.project.gymtopia.trainer.data.model.TrainerResponse;
import com.project.gymtopia.trainer.service.TrainerAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainer")
public class TrainerJoinController {

  private final TrainerAuthService trainerAuthService;

  @PostMapping("/signUp")
  public ResponseEntity<TrainerResponse> memberSignUp(@RequestBody UserSignUpForm userSignUpForm) {

    TrainerResponse trainerResponse = trainerAuthService.signUp(userSignUpForm);

    return ResponseEntity.ok(trainerResponse);
  }

  @PostMapping("/signIn")
  public ResponseEntity<TokenResponse> memberSignIn(@RequestBody UserSignInForm userSignInForm) {

    TrainerDto trainerDto = trainerAuthService.authenticate(userSignInForm.getEmail(),
        userSignInForm.getPassword());

    // 톤큰 생성 후 발행
    TokenResponse tokenResponse = trainerAuthService.createToken(trainerDto);

    return ResponseEntity.ok(tokenResponse);
  }


}
