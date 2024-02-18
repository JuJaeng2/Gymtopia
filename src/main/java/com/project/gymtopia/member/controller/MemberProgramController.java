package com.project.gymtopia.member.controller;

import com.project.gymtopia.trainer.service.SpecialProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Member Program Controller", description = "회원이 특별 프로그램을 조회 및 신청하는 컨트롤러")
public class MemberProgramController {

  private final SpecialProgramService specialProgramService;

  /**
   * 특별프로그램 목록을 조회
   */
  @Operation(summary = "특별프로그램 목록을 조회")
  @GetMapping
  public ResponseEntity<?> findProgram(Authentication authentication){
    return ResponseEntity.ok(specialProgramService.findAllSpecialProgram(authentication.getName()));
  }

  /**
   * 특별프로그램 신청
   */
  @Operation(summary = "특별프로그램 신청")
  @PostMapping("/{programId}")
  public ResponseEntity<?> applyProgram(Authentication authentication, @PathVariable(value = "programId") long programId){

    specialProgramService.applyProgram(authentication.getName(), programId);

    return ResponseEntity.ok("신청이 완료되었습니다.");
  }
}
