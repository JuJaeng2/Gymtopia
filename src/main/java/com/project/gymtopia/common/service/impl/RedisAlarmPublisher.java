package com.project.gymtopia.common.service.impl;

import com.project.gymtopia.common.data.model.MessageDto;
import com.project.gymtopia.common.service.AlarmPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisAlarmPublisher implements AlarmPublisher {

  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void sendRedisAlarm(MessageDto messageDto){
    redisTemplate.convertAndSend("Alarm", messageDto);
  }


}
