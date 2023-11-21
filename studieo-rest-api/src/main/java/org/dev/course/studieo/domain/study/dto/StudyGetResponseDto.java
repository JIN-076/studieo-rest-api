package org.dev.course.studieo.domain.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dev.course.studieo.domain.study.entity.CategoryType;
import org.dev.course.studieo.domain.study.entity.StudyType;

import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class StudyGetResponseDto {

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
}
