package com.project.gymtopia.member.controller;

import com.project.gymtopia.member.data.model.MemberResponse;
import com.project.gymtopia.member.data.model.MemberUpdate;
import com.project.gymtopia.member.service.MemberInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
@Tag(name = "Member Information Controller", description = "회원의 정보를 확인하고 수정하는 컨트롤러")
public class MemberInfoController {

  private final MemberInfoService memberInfoService;

  @Operation(summary = "회원 정보 확인")
  @GetMapping("/info")
  public ResponseEntity<MemberResponse> memberInformation(Authentication authentication
  ) {

    String email = getEmail(authentication);
    log.info("Email >>>" + email);
    MemberResponse memberResponse = memberInfoService.getMemberInformation(email);
    return ResponseEntity.ok(memberResponse);
  }

  @Operation(summary = "회원 정보 수정")
  @PutMapping("/info")
  public ResponseEntity<MemberResponse> updateMemberInformation(
      @RequestBody MemberUpdate memberUpdate, Authentication authentication
  ){
    String email = getEmail(authentication);
    MemberResponse memberResponse = memberInfoService.updateInfo(memberUpdate, email);
    return ResponseEntity.ok(memberResponse);
  }

  private String getEmail(Authentication authentication){
    return authentication.getName();
  }

}
