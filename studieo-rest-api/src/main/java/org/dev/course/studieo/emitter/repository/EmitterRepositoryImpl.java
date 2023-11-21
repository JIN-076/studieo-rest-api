package org.dev.course.studieo.emitter.repository;

import org.dev.course.studieo.domain.notification.entity.Notification;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Notification> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String id, Notification notification) {
        eventCache.put(id, notification);
    }

    @Override
    public Map<String, Notification> findAllEventCacheStartWithId(String id) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithById(String id) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public void deleteAllEmitterStartWithId(String id) {
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(id)) {
                emitters.remove(key);
            }
        });
    }

    @Override
    public void deleteAllEventCacheStartWithId(String id) {
        eventCache.forEach((key, emitter) -> {
            if (key.startsWith(id)) {
                eventCache.remove(key);
            }
        });
    }
}
