package org.dev.course.studieo.domain.user.validator;

import lombok.RequiredArgsConstructor;
import org.dev.course.studieo.domain.user.dto.UserEditRequestDto;
import org.dev.course.studieo.domain.user.dto.UserRequestDto;
import org.dev.course.studieo.domain.user.dto.UserSaveRequestDto;
import org.dev.course.studieo.domain.user.entity.User;
import org.dev.course.studieo.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class CheckNickNameValidator extends AbstractValidator<UserRequestDto> {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    protected void doValidate(UserRequestDto dto, Errors errors) {
        if (dto instanceof UserSaveRequestDto) {
            validateSave((UserSaveRequestDto) dto, errors);
        } else if (dto instanceof UserEditRequestDto) {
            validateEdit((UserEditRequestDto) dto, errors);
        }
    }

    private void validateSave(UserSaveRequestDto dto, Errors errors) {
        User user = userRepository.findByNickname(dto.getNickname()).orElse(null);
        if (user != null && !user.getLoginId().equals(dto.getLoginId())) {
            errors.rejectValue("nickname", "닉네임 중복 오류", "이미 사용중인 닉네임입니다.");
        }
    }

    private void validateEdit(UserEditRequestDto dto, Errors errors) {
        User user = userRepository.findByNickname(dto.getNickname()).orElse(null);
        if (user != null && !user.getLoginId().equals(dto.getLoginId())) {
            errors.rejectValue("nickname", "닉네임 중복 오류", "이미 사용중인 닉네임입니다.");
        }
    }
}
