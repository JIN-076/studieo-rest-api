package org.dev.course.studieo.domain.study.entity;

import org.dev.course.studieo.domain.study.exception.StudyTypeNotFoundException;

import java.util.Arrays;

public enum StudyType {

    ONLINE(1),
    OFFLINE(2);

    private final int value;

    StudyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isOffline(String studiesType) {
        return OFFLINE.getValue() == StudyType.valueOf(studiesType).getValue();
    }

    public static StudyType valueOf(int value) {
        return Arrays.stream(values())
                .filter(studyType -> studyType.getValue() == value)
                .findAny()
                .orElseThrow(() -> {
                    throw new StudyTypeNotFoundException();
                });
    }
}
