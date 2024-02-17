package com.project.gymtopia.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisAlarmService {

  private final RedisTemplate<String, Object> redisTemplate;
  private final ChannelTopic topic;

  public void alarmTrainer(String memberId, String message){
    redisTemplate.convertAndSend(topic.getTopic(), memberId + ":" + message);
  }


}
