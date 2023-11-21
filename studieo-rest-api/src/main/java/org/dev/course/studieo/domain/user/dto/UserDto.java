package org.dev.course.studieo.domain.user.dto;

import org.dev.course.studieo.domain.user.entity.vo.Email;
import org.dev.course.studieo.domain.user.entity.vo.Tel;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record UserDto(
        UUID userId,
        String loginId,
        String name,
        Email email,
        Integer age,
        String nickname,
        String password,
        Tel tel,
        Boolean sex,
        LocalDate birthDate
) {
    public UserDto {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(loginId);
        Objects.requireNonNull(name);
        Objects.requireNonNull(email);
        Objects.requireNonNull(age);
        Objects.requireNonNull(nickname);
        Objects.requireNonNull(password);
        Objects.requireNonNull(tel);
        Objects.requireNonNull(sex);
        Objects.requireNonNull(birthDate);
    }
}
