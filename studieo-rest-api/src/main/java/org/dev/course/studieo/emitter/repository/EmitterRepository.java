package org.dev.course.studieo.emitter.repository;

import org.dev.course.studieo.domain.notification.entity.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

    SseEmitter save(String id, SseEmitter sseEmitter);
    void saveEventCache(String id, Notification notification);
    Map<String, Notification> findAllEventCacheStartWithId(String studyId);
    Map<String, SseEmitter> findAllEmitterStartWithById(String id);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(String id);
    void deleteAllEventCacheStartWithId(String id);
}
