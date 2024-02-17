package com.project.gymtopia.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gymtopia.common.data.AlarmType;
import com.project.gymtopia.common.data.model.MessageDto;
import com.project.gymtopia.common.repository.SseRepository;
import com.project.gymtopia.common.service.AlarmService;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Getter
@Slf4j
@RequiredArgsConstructor
public class RedisAlarmSubscriber implements MessageListener {

  private static List<String> messageList = new ArrayList<>();
  private final ObjectMapper mapper = new ObjectMapper();
  private final SseRepository sseRepository;
  private final AlarmService alarmService;

  @Override
  public void onMessage(Message message, byte[] pattern) {

    try {
      MessageDto messageDto = mapper.readValue(message.getBody(), MessageDto.class);
      messageList.add(message.toString());

      // 1. sseEmitter 찾기
      SseEmitter emitterId = sseRepository.findEmitterById(messageDto.getEmitterId());
      if (emitterId != null) {
        // 2. 값이 있다면 알림 보내기
        if (messageDto.getAlarmType() == AlarmType.JOURNAL){
          alarmService.sendJournalAlarm(
              messageDto.getMember(), messageDto.getTrainer(), messageDto.getMessage());
        }else {
          alarmService.sendMissionAlarm(
              messageDto.getMember(), messageDto.getTrainer(), messageDto.getMessage());
        }


      }

      log.info("받은 메세지 : {}", messageDto.getEmitterId());
      log.info("messageList : {}", messageList);
    } catch (Exception e) {
      log.error("Redis Subscriber Error : {}", e.getMessage());
    }

  }
}
