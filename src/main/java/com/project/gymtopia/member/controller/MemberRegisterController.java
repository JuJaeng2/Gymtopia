package com.project.gymtopia.member.controller;

import com.project.gymtopia.member.data.model.RegisterForm;
import com.project.gymtopia.member.service.MemberRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberRegisterController {

  private final MemberRegisterService memberRegisterService;


  /**
   * 원하는 트레이너에게 운동을 신청하는 API
   */
  @PostMapping("/register/trainer/{trainerId}")
  public ResponseEntity<?> register(
      Authentication authentication,
      @PathVariable(value = "trainerId") long trainerId,
      @RequestBody RegisterForm registerForm
  ){
    String email = authentication.getName();
    memberRegisterService.register(email, trainerId, registerForm);
    return ResponseEntity.ok("신청이 완료되었습니다.");
  }

}
