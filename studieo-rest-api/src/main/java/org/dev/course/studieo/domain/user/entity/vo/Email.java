package org.dev.course.studieo.domain.user.entity.vo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Email {

    private final String email;

    public Email(String email) {
        this.email = email;
    }

    public static Email valueOf(String email) {
        return new Email(email);
    }
}
