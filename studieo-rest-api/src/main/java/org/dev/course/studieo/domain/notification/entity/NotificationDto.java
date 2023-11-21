package org.dev.course.studieo.domain.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDto(
        UUID studyId,
        UUID receiverId,
        UUID senderId,
        String content,
        LocalDateTime createdAt,
        boolean isRead
) {
}
