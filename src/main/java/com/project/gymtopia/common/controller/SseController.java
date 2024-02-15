package com.project.gymtopia.common.controller;

import com.project.gymtopia.common.data.MissionState;
import com.project.gymtopia.common.data.entity.Mission;
import com.project.gymtopia.common.repository.MissionRepository;
import com.project.gymtopia.common.roles.Roles;
import com.project.gymtopia.common.service.AlarmService;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.trainer.data.entity.Trainer;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sse")
public class SseController {

  private final AlarmService alarmService;

  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public SseEmitter subscribe(
      Authentication authentication,
      HttpServletRequest request,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
  ) {
    String email = authentication.getName();

    log.info("Last-Event-Id : {}", lastEventId);

    return alarmService.connect(email, lastEventId, request);
  }

  private final MemberRepository memberRepository;
  private final MissionRepository missionRepository;

  @PostMapping
  public void sendMessage(Authentication authentication){
    Member member = memberRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    List<Mission> missionList = missionRepository.findByMemberAndState(member, MissionState.PROGRESSING);

    Trainer trainer = missionList.get(0).getTrainer();
    alarmService.send(member, trainer,"테스트 메세지 확인", Roles.MEMBER);
  }
}
