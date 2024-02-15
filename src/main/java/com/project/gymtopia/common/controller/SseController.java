package com.project.gymtopia.common.controller;

import com.project.gymtopia.common.service.AlarmService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
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

    return alarmService.subscribe(email, lastEventId, request);
  }


}
