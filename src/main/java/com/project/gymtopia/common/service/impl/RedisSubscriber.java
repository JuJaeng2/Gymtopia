package com.project.gymtopia.common.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String alarm = new String(message.getBody());
    log.info("받은 알림 메세지 : " + alarm);
    // 트레이너에
  }
}
