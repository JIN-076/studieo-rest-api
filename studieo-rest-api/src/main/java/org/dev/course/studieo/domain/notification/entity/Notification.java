package org.dev.course.studieo.domain.notification.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class Notification {

    private final UUID studyId;
    private final UUID receiverId;
    private final UUID senderId;
    private final String content;
    private final LocalDateTime createdAt;
    private final Boolean isRead;
}
