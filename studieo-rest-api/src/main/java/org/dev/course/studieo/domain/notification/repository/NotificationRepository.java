package org.dev.course.studieo.domain.notification.repository;

import org.dev.course.studieo.domain.notification.entity.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {

    Notification save(Notification notification);
    Optional<Notification> findById(UUID studyId, UUID receiverId, UUID senderId);
    List<Notification> findByStudyId(UUID studyId);
    List<Notification> findByReceiverId(UUID receiverId);
    void delete(UUID studyId, UUID senderId);
}
