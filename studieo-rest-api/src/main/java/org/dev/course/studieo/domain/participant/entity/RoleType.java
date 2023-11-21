package org.dev.course.studieo.domain.participant.entity;

import lombok.Getter;
import org.dev.course.studieo.domain.participant.exception.RoleNotFoundException;

import java.util.Arrays;

@Getter
public enum RoleType {

    NONE(0),
    TEAM_LEADER(1),
    TEAM_MEMBER(2),
    TEAM_APPLICANT(3);

    private final int value;

    RoleType(int value) {
        this.value = value;
    }

    public static RoleType valueOf(int value) {
        return Arrays.stream(values())
                .filter(roleType -> roleType.getValue() == value)
                .findAny()
                .orElseThrow(RoleNotFoundException::new);
    }
}
