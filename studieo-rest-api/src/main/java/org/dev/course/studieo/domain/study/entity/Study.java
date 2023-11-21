package org.dev.course.studieo.domain.study.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
public class Study {

    private final UUID studyId;
    private final String name;
    private final StudyType studyType;
    private final String description;
    private final String subject;
    private final CategoryType category;
    private final String requirement;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final int capacity;
    private final int headCount;
    private final int times;
    private final boolean isAccept;

    public Study(
            UUID studyId,
            String name,
            StudyType studyType,
            String description,
            String subject,
            CategoryType category,
            String requirement,
            String address,
            Double latitude,
            Double longitude,
            int capacity,
            int headCount,
            int times,
            boolean isAccept
    ) {
        this.studyId = studyId;
        this.name = name;
        this.studyType = studyType;
        this.description = description;
        this.subject = subject;
        this.category = category;
        this.requirement = requirement;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacity = capacity;
        this.headCount = headCount;
        this.times = times;
        this.isAccept = isAccept;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Pair {
        private final Double x;
        private final Double y;
    }

    public boolean isRequireAccept() {
        return this.isAccept;
    }
}


