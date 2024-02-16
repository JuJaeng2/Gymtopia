package com.project.gymtopia.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.gymtopia.common.data.AlarmType;
import com.project.gymtopia.common.data.model.MessageDto;
import com.project.gymtopia.common.repository.SseRepository;
import com.project.gymtopia.common.service.AlarmService;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.member.repository.MemberRepository;
import com.project.gymtopia.trainer.data.entity.Trainer;
import com.project.gymtopia.trainer.repository.TrainerRepository;
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
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final SseRepository sseRepository;
  private final AlarmService alarmService;
  private final MemberRepository memberRepository;
  private final TrainerRepository trainerRepository;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    objectMapper.registerModule(new JavaTimeModule());
    try {
      log.info("Message.body from Redis : {}", message);
      log.info("Message.body 역직렬화 : {}", objectMapper.readValue(message.getBody(), MessageDto.class).getMessage());
      MessageDto messageDto =
          objectMapper.registerModule(new JavaTimeModule()).readValue(message.getBody(), MessageDto.class);
      messageList.add(message.toString());

      // 1. sseEmitter 찾기
      SseEmitter emitterId = sseRepository.findEmitterById(messageDto.getEmitterId());
      if (emitterId != null) {

        Trainer trainer = trainerRepository.findById(messageDto.getTrainerDto().getId())
            .orElseThrow(() -> new CustomException(ErrorCode.TRAINER_NOT_FOUND));
        Member member = memberRepository.findById(messageDto.getMemberDto().getId())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 값이 있다면 알림 보내기
        if (messageDto.getAlarmType() == AlarmType.JOURNAL){
          alarmService.sendJournalAlarm(
              member, trainer, messageDto.getMessage());
        }else {
          alarmService.sendMissionAlarm(
              member, trainer, messageDto.getMessage());
        }
      }else {
        log.info("존재하는 SseEmitter가 없습니다!!");
      }

      log.info("받은 메세지 : {}", messageDto.getEmitterId());
    } catch (Exception e){
      e.printStackTrace();
    }

  }
}
