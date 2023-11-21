package org.dev.course.studieo.domain.participant.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
public class Participant {

    private final UUID studyId;
    private final UUID userId;
    private RoleType role;

    public Participant(UUID studyId, UUID userId, RoleType role) {
        this.studyId = studyId;
        this.userId = userId;
        this.role = role;
    }

    public boolean isEqual(RoleType role) {
        return this.role.equals(role);
    }

    public void setMember() {
        this.role = RoleType.TEAM_MEMBER;
    }
}
