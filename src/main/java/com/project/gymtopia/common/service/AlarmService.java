package com.project.gymtopia.common.service;

import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.trainer.data.entity.Trainer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmService {

  SseEmitter connect(String email, String lastEventId, HttpServletRequest request);

  void sendJournalAlarm(Member member, Trainer trainer, String contents);

  void sendMissionAlarm(Member member, Trainer trainer, String contents);

  boolean findEmitter(String emitterId);
}
