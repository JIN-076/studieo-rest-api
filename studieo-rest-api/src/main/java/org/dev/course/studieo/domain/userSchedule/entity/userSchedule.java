package org.dev.course.studieo.domain.userSchedule.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
public class userSchedule {

    private final UUID userId;
    private final UUID scheduleId;

    public userSchedule(UUID userId, UUID scheduleId) {
        this.userId = userId;
        this.scheduleId = scheduleId;
    }
}
