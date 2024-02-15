package com.project.gymtopia.common.service.impl;

import com.project.gymtopia.common.data.AlarmType;
import com.project.gymtopia.common.data.entity.Alarm;
import com.project.gymtopia.common.data.model.AlarmResponse;
import com.project.gymtopia.common.repository.AlarmRepository;
import com.project.gymtopia.common.repository.SseRepository;
import com.project.gymtopia.common.roles.Roles;
import com.project.gymtopia.common.service.AlarmService;
import com.project.gymtopia.config.jwt.JwtToken;
import com.project.gymtopia.exception.CustomException;
import com.project.gymtopia.exception.ErrorCode;
import com.project.gymtopia.member.data.entity.Member;
import com.project.gymtopia.trainer.data.entity.Trainer;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmServiceImpl implements AlarmService {

  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

  private final AlarmRepository alarmRepository;
  private final SseRepository sseRepository;
  private final JwtToken jwtToken;

  /**
   * SSE 구독
   */
  @Override
  public SseEmitter connect(String email, String lastEventId, HttpServletRequest request) {

    String token = jwtToken.getTokenFromRequest(request);
    long userId = jwtToken.getId(token);
    String emitterId = userId + "_" + email;

    SseEmitter sseEmitter = sseRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

    sseEmitter.onCompletion(() -> {
      log.info("sseEmitter 연결 종료");
      sseRepository.deleteById(emitterId);
    });
    sseEmitter.onTimeout(() -> {
      log.info("sseEmitter Timeout");
      sseRepository.deleteById(emitterId);
    });

    //503 에러를 방지하기 위한 더미 이벤트 전송
    sendDummyToClient(sseEmitter, emitterId, "SSE 연결 성공. [userId = " + userId + "]");

    //수신되지 않은 event 목록이 존재할 경우 전송하여 event 유실 예방
    if (!lastEventId.isEmpty()) {
      Map<String, Object> eventCaches =
          sseRepository.findAllEventCacheStartWithId(emitterId);
      eventCaches.forEach((key, value) -> sendToClient(sseEmitter, key, value));

      // 미수신된 이벤트가 모두 전송되면 케쉬에 저장되어있던 이벤트 삭제
      sseRepository.deleteAllEventCacheStartWithId(emitterId);
    }
    return sseEmitter;
  }

  /**
   * 503 에러를 막기 위한 더미 데이터 전송
   */
  private void sendDummyToClient(SseEmitter sseEmitter, String id, Object data) {
    try {
      sseEmitter.send(SseEmitter.event()
          .id(id)
          .name("SSE Dummy")
          .data(data));
    } catch (IOException exception) {
      sseRepository.deleteById(id);
      throw new RuntimeException("연결 오류!");
    }
  }

  /**
   * 알림 보내기
   */
  @Override
  public void send(Member member, Trainer trainer, String contents, Roles receiver) {
    log.info("알람을 보냅니다. 알람 내용 : {}", contents);

    Map<String, Object> map =
        Map.of(
            "alarmType", receiver == Roles.TRAINER ? AlarmType.JOURNAL : AlarmType.MISSION,
            "emitterId", receiver == Roles.TRAINER ?
                makeId(trainer.getId(), trainer.getEmail()) : makeId(member.getId(), member.getEmail()));

    Alarm alarm = Alarm.builder()
        .contents(contents)
        .member(member)
        .trainer(trainer)
        .alarmType((AlarmType) map.get("alarmType"))
        .createDateTime(LocalDateTime.now())
        .build();

    String emitterId = String.valueOf(map.get("emitterId"));
    AlarmResponse alarmResponse = AlarmResponse.from(alarm);
    sseRepository.saveCache(emitterId + System.currentTimeMillis(), alarmResponse);

    SseEmitter sseEmitter = sseRepository.findEmitterById(emitterId);
    if (sseEmitter == null){
      return;
    }

    sendToClient(sseEmitter, emitterId, alarmResponse);

    alarmRepository.save(alarm);

  }

  private void sendToClient(SseEmitter sseEmitter, String emitterId, Object data) {
    try {
      sseEmitter.send(SseEmitter.event()
          .id(emitterId)
          .name("SSE")
          .data(data));

      sseRepository.deleteAllEventCacheStartWithId(emitterId);

    } catch (IOException e) {
      log.info("알림 전송 에러 : IOException 발생");
      sseRepository.deleteById(emitterId);
      throw new CustomException(ErrorCode.SSE_CONNECTION_ERROR);
    }
  }

  private String makeId(long id, String email) {
    return id + "_" + email;
  }
}
