package org.dev.course.studieo.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.notification.entity.Notification;
import org.dev.course.studieo.domain.notification.entity.NotificationDto;
import org.dev.course.studieo.domain.notification.repository.NotificationRepository;
import org.dev.course.studieo.domain.user.entity.vo.SessionUser;
import org.dev.course.studieo.emitter.repository.EmitterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter connect(UUID userId, String lastEventId) {

        //String id = userId + "_" + System.currentTimeMillis();
        String id = userId.toString();
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(-1L));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // send dummyData to avoid 503 Err
        String dummyData = "EventStream Created. [userId=" + userId + "]";
        sendToClient(emitter, id, dummyData);

        if (!lastEventId.isEmpty()) {
            Map<String, Notification> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;
    }

    public void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            e.printStackTrace();
            emitterRepository.deleteById(id);
            throw new RuntimeException("Failed to connect!");
        }
    }

    // send teamLeader from server for notification
    public Notification send(SessionUser user, UUID studyId, UUID receiverId, String content) {

        Notification notification = createNotification(studyId, receiverId, user.getUserId(), content);
        save(notification);
        String id = receiverId.toString();
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithById(id);

        sseEmitters.forEach((key, emitter) -> {
            emitterRepository.saveEventCache(key, notification);
            sendToClient(emitter, key, notification);
        });
        return notification;
    }

    public List<Notification> findAllNotificationByUserId(NotificationDto notificationDto) {
        return notificationRepository.findByReceiverId(notificationDto.receiverId());
    }

    public void delete(NotificationDto notificationDto) {
        notificationRepository.delete(notificationDto.studyId(), notificationDto.senderId());
    }

    private Notification createNotification(UUID studyId, UUID receiverId, UUID senderId, String content) {
        return Notification.builder()
                .studyId(studyId)
                .receiverId(receiverId)
                .senderId(senderId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
    }

    private void save(Notification notification) {
        notificationRepository.save(notification);
    }
}
