package org.dev.course.studieo.domain.study.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.dev.course.studieo.domain.study.entity.StudyType;

import java.util.UUID;

@Getter
@Builder
public class StudySaveRequestDto {

    private final UUID studyId;

    @NotBlank
    private final String name;

    @NotBlank
    private final String studyType;

    @NotBlank
    private final String description;

    @NotBlank
    private final String subject;

    @NotBlank
    private final String category;

    private final String requirement;
    private final String address;

    @NotBlank
    private final String capacity;

    @NotBlank
    private final String times;

    @NotBlank
    private final String isAccept;

    public boolean isEqual(StudyType studiesType) {
        return StudyType.valueOf(this.studyType).equals(studiesType);
    }
}
