package com.project.gymtopia.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
  private final ObjectMapper objectMapper = new ObjectMapper();
  @Override
  public void sendRedisAlarm(MessageDto messageDto){

    try{
//      String jsonMessage = objectMapper
//          .registerModule(new JavaTimeModule())
//          .writeValueAsString(messageDto);
//      log.info("Serialize : {}", jsonMessage);

      redisTemplate.convertAndSend("Alarm", messageDto);
    }catch (Exception e){
      log.error("Serialize Error : {}", e.getMessage());
    }


  }


}
