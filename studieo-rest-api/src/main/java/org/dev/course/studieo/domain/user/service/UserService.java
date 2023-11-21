package org.dev.course.studieo.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.user.dto.UserDto;
import org.dev.course.studieo.domain.user.dto.UserLoginRequestDto;
import org.dev.course.studieo.domain.user.entity.User;
import org.dev.course.studieo.domain.user.exception.LoginFailureException;
import org.dev.course.studieo.domain.user.exception.UserNotFoundException;
import org.dev.course.studieo.domain.user.mapper.UserMapper;
import org.dev.course.studieo.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signUp(UserDto userDto) {
        User user = UserMapper.INSTANCE.mapToEntity(userDto);
        return save(user);
    }

    public User findByLoginId(String loginId) {
        return get(loginId);
    }

    public User findByUserId(UUID userId) {
        return getByUserId(userId);
    }

    public User login(UserLoginRequestDto loginRequestDto) {

        User user = get(loginRequestDto.getLoginId()); // if non-exists, throw UserNotFoundException
        boolean isLoginSuccess = user.isPasswdEqual(loginRequestDto.getPassword());
        if (!isLoginSuccess) throw new LoginFailureException(); // if non-equal passwd, throw LoginFailureException
        return user;
    }

    @Transactional
    public User update(UserDto userDto) {
        User user = UserMapper.INSTANCE.mapToEntity(userDto);
        return update(user);
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    private User get(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> {
                    log.warn("No User found for loginId '{}'", loginId);
                    return new UserNotFoundException();
                });
    }

    private User getByUserId(UUID userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("No User found for userId '{}'", userId.toString());
                    return new UserNotFoundException();
                });
    }

    private User update(User user) {
        return userRepository.update(user);
    }

    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validators = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validators.put(validKeyName, error.getDefaultMessage());
        }
        return validators;
    }
}
