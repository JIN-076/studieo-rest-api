package org.dev.course.studieo.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import org.dev.course.studieo.domain.user.entity.vo.Email;
import org.dev.course.studieo.domain.user.entity.vo.Tel;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class User {

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

    public User(
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
        this.userId = userId;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.age = age;
        this.nickname = nickname;
        this.password = password;
        this.tel = tel;
        this.sex = sex;
        this.birthDate = birthDate;
    }

    public boolean isPasswdEqual(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    public boolean isUserIdEqual(UUID userId) {
        return this.userId.equals(userId);
    }
}
