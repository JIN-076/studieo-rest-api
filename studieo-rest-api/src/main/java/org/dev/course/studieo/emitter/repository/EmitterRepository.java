package org.dev.course.studieo.emitter.repository;

import org.dev.course.studieo.domain.notification.entity.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;

public interface EmitterRepository {

    SseEmitter save(String id, SseEmitter sseEmitter); // save Emitter
    void saveEventCache(String id, Notification notification); // save Event
    Map<String, Notification> findAllEventCacheStartWithId(String studyId); // find all emitter about studyId
    Map<String, SseEmitter> findAllEmitterStartWithById(String id); // find all event about studyId
    void sendNotificationToUser(UUID userId, String data);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(String id);
    void deleteAllEventCacheStartWithId(String id);
}
