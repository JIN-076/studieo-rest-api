package org.dev.course.studieo.domain.participant.dto;

import org.dev.course.studieo.domain.participant.entity.RoleType;

import java.util.UUID;

public record ParticipantDto(
        UUID studyId,
        UUID userId,
        RoleType role
) {
}
