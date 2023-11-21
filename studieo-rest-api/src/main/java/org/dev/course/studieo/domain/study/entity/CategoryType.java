package org.dev.course.studieo.domain.study.entity;

import org.dev.course.studieo.domain.study.exception.CategoryTypeNotFoundException;

import java.util.Arrays;

public enum CategoryType {

    IT("IT 계열", 1),
    MARKETING("마케팅", 2),
    HISTORY("역사", 3),
    LANGUAGE("언어", 4),
    MATHEMATICS("수학", 5),
    FINANCE("금융", 6),
    LAW("법학", 7),
    CIVIL_SERVICE("공무원", 8),
    BAR_EXAM("사법고시", 9),
    AI("인공지능", 10),
    LITERATURE("문학", 11);

    private final String detail;
    private final int value;

    CategoryType(String detail, int value) {
        this.detail = detail;
        this.value = value;
    }

    public String getDetail() {
        return detail;
    }

    public int getValue() {
        return value;
    }

    public static CategoryType valueOf(int value) {
        return Arrays.stream(values())
                .filter(category -> category.getValue() == value)
                .findAny()
                .orElseThrow(CategoryTypeNotFoundException::new);
    }
}
