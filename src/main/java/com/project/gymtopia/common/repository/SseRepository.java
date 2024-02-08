package com.project.gymtopia.common.repository;

import com.project.gymtopia.common.data.model.AlarmResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
@Slf4j
public class SseRepository {

  private final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
  private final Map<String, Object> cacheMap = new ConcurrentHashMap<>();

  public SseEmitter save(String emitterId, SseEmitter sseEmitter){
    sseEmitterMap.put(emitterId, sseEmitter);
    log.info("=====save()=====");
    log.info("SseEmitter Map >> {}", sseEmitterMap);
    log.info("Cache Map >> {}", cacheMap);
    return sseEmitter;
  }

  public void saveCache(String emitterId, AlarmResponse alarmResponse){
    cacheMap.put(emitterId, alarmResponse);
    log.info("=====saveCache()=====");
    log.info("SseEmitter Map >> {}", sseEmitterMap);
    log.info("Cache Map >> {}", cacheMap);
  }

  public Map<String, SseEmitter> findAllEmitterStartWithId(String id) { // 해당 회원과 관련된 모든 이벤트를 찾음
    return sseEmitterMap.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(id))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public Map<String, Object> findAllEventCacheStartWithId(String id) {
    return cacheMap.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(id))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public void deleteById(String id) {
    sseEmitterMap.remove(id);
  }

  // 해당 회원과 관련된 모든 emitter를 지움
  public void deleteAllEmitterStartWithId(String id) {
    sseEmitterMap.forEach(
        (key, emitter) -> {
          if (key.startsWith(id)) {
            sseEmitterMap.remove(key);
          }
        }
    );
  }

  public void deleteAllEventCacheStartWithId(String id) { // 해당 회원과 관련된 모든 이벤트를 지움
    cacheMap.forEach(
        (key, emitter) -> {
          if (key.startsWith(id)) {
            cacheMap.remove(key);
          }
        }
    );
  }
}
