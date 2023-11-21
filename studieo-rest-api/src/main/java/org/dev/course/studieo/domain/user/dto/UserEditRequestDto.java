package org.dev.course.studieo.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

@Slf4j
@Getter
@Builder
public class UserEditRequestDto implements UserRequestDto {

    private final UUID userId;

    private final String loginId;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 10, message = "이름은 2 ~ 10자 사이로 입력해주세요.")
    private final String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private final String email;

    @NotBlank(message = "나이를 입력해주세요.")
    @Range(min = 1, max = 100)
    private final String age;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 15, message = "닉네임은 2 ~ 15자 사이로 입력해주세요.")
    private final String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$",
            message = "입력하신 전화번호가 유효하지 않습니다. XXX-XXXX-XXXX 형태로 입력해주세요.")
    private final String tel;
    @NotBlank(message = "성별을 선택해주세요.")
    private final String sex;

    private final String birthDate;
}
