package org.dev.course.studieo.domain.schedule.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
public class Schedule {

    private final UUID scheduleId;
    private final UUID studyId;
    private final int startTime;
    private final int endTime;
    private final int day;
    private final int period;
    private final String title;
    private final String description;

    public Schedule(
            UUID scheduleId,
            UUID studiesId,
            int startTime,
            int endTime,
            int day,
            int period,
            String title,
            String description
    ) {
        this.scheduleId = scheduleId;
        this.studyId = studiesId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.period = period;
        this.title = title;
        this.description = description;
    }
}
