package com.project.gymtopia.common.service;

import com.project.gymtopia.common.data.model.MessageDto;

public interface AlarmPublisher {

  void sendRedisAlarm(MessageDto messageDto);
}
