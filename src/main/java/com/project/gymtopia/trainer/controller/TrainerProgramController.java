package com.project.gymtopia.trainer.controller;

import com.project.gymtopia.trainer.data.model.SpecialProgramForm;
import com.project.gymtopia.trainer.service.SpecialProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainer/program")
public class TrainerProgramController {

  private final SpecialProgramService specialProgramService;

  /**
   * 특별 프로그램 등록
   */
  @PostMapping
  public ResponseEntity<?> register(
      Authentication authentication,
      @RequestBody SpecialProgramForm specialProgramForm
  ){

    specialProgramService.registerProgram(authentication.getName(), specialProgramForm);
    return ResponseEntity.ok("특별프로그램 등록이 완료되었습니다.");
  }

  /**
   * 특별프로그램 모집 마감 수동 처리
   */
  @PutMapping("/{programId}")
  public ResponseEntity<?> closeProgram(
      Authentication authentication,
      @PathVariable(value = "programId") long programId){
    specialProgramService.closeProgram(authentication.getName(), programId);
    return ResponseEntity.ok("특별프로그램 모집을 마감하였습니다.");
  }
}
