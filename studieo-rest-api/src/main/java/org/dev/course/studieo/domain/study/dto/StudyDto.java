package org.dev.course.studieo.domain.study.dto;

import org.dev.course.studieo.domain.study.entity.CategoryType;
import org.dev.course.studieo.domain.study.entity.StudyType;

import java.util.Objects;

public record StudyDto(String name,
                       StudyType studyType,
                       String description,
                       String subject,
                       CategoryType category,
                       String requirement,
                       String address,
                       Double latitude,
                       Double longitude,
                       Integer capacity,
                       Integer times,
                       Boolean isAccept) {

    public StudyDto {
        Objects.requireNonNull(name);
        Objects.requireNonNull(studyType);
        Objects.requireNonNull(description);
        Objects.requireNonNull(subject);
        Objects.requireNonNull(category);
        Objects.requireNonNull(capacity);
        Objects.requireNonNull(times);
        Objects.requireNonNull(isAccept);
    }
}
