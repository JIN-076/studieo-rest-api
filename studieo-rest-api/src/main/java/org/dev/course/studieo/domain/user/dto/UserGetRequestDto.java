package org.dev.course.studieo.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.user.entity.vo.Email;
import org.dev.course.studieo.domain.user.entity.vo.Tel;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Getter
@Builder
@RequiredArgsConstructor
public class UserGetRequestDto implements UserRequestDto {

    private final UUID userId;
    private final String loginId;
    private final String name;
    private final Email email;
    private final Integer age;
    private final String nickname;
    private final String password;
    private final Tel tel;
    private final Boolean sex;
    private final LocalDate birthDate;
}
