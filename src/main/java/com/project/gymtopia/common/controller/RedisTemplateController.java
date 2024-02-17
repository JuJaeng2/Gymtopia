package com.project.gymtopia.common.controller;

import com.project.gymtopia.common.data.model.MessageDto;
import com.project.gymtopia.common.service.impl.RedisAlarmPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisTemplateController {

  private final RedisAlarmPublisher redisAlarmPublisher;

  @PostMapping("/publish")
  public void sendAlarm(Authentication authentication, @RequestBody MessageDto messageDto) {
    redisAlarmPublisher.sendRedisAlarm(messageDto);
  }

}
