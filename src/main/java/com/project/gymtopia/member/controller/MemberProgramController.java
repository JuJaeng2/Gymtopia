package com.project.gymtopia.member.controller;

import com.project.gymtopia.trainer.service.SpecialProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/program")
public class MemberProgramController {

  private final SpecialProgramService specialProgramService;

  /**
   * 특별프로그램 목록을 조회
   */
  @GetMapping
  public ResponseEntity<?> findProgram(Authentication authentication){
    return ResponseEntity.ok(specialProgramService.findAllSpecialProgram(authentication.getName()));
  }

  /**
   * 특별프로그램 신청
   */

  @PostMapping("/{programId}")
  public ResponseEntity<?> applyProgram(Authentication authentication, @PathVariable(value = "programId") long programId){

    specialProgramService.applyProgram(authentication.getName(), programId);

    return ResponseEntity.ok("신청이 완료되었습니다.");
  }
}
