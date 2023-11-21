package org.dev.course.studieo.domain.user.validator;

import lombok.RequiredArgsConstructor;
import org.dev.course.studieo.domain.user.dto.UserSaveRequestDto;
import org.dev.course.studieo.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckLoginIdValidator extends AbstractValidator<UserSaveRequestDto> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserSaveRequestDto dto, Errors errors) {
        if (userRepository.findByLoginId(dto.getLoginId()).isPresent()) {
            errors.rejectValue("loginId", "로그인 아이디 중복 오류", "이미 사용중인 아이디입니다.");
        }
    }
}
