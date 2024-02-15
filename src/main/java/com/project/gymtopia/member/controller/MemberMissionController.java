package com.project.gymtopia.member.controller;

import com.project.gymtopia.member.data.model.MissionResponse;
import com.project.gymtopia.member.service.MemberMissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberMissionController {

  private final MemberMissionService memberMissionService;

  /**
   * 회원에게 부여된 미션을 확인하는 API
   */
  @GetMapping("/mission")
  public ResponseEntity<?> getAllMission(Authentication authentication) {
    String email = authentication.getName();
    List<MissionResponse> missionResponseList = memberMissionService.getAllMission(email);
    return ResponseEntity.ok(missionResponseList);
  }

}
