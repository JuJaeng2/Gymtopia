package com.project.gymtopia.common.controller;

import com.project.gymtopia.common.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sse")
@Tag(name = "SSE Controller", description = "SSE 통신을 위한 컨트롤러")
public class SseController {

  private final AlarmService alarmService;

  @Operation(summary = "SSE 구독(연결)")
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

//
}
