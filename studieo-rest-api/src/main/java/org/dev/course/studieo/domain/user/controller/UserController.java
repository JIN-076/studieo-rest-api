package org.dev.course.studieo.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.user.dto.*;
import org.dev.course.studieo.domain.user.entity.User;
import org.dev.course.studieo.domain.user.entity.vo.SessionUser;
import org.dev.course.studieo.domain.user.exception.LoginFailureException;
import org.dev.course.studieo.domain.user.mapper.UserMapper;
import org.dev.course.studieo.domain.user.service.UserService;
import org.dev.course.studieo.domain.user.validator.CheckEmailValidator;
import org.dev.course.studieo.domain.user.validator.CheckLoginIdValidator;
import org.dev.course.studieo.domain.user.validator.CheckNickNameValidator;
import org.dev.course.studieo.util.converter.BCryptConverter;
import org.dev.course.studieo.util.generator.UUIDGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static org.dev.course.studieo.constants.Constants.SESSION_KEY;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UUIDGenerator uuidGenerator;
    private final BCryptConverter bCryptConverter;

    private final CheckLoginIdValidator checkLoginIdValidator;
    private final CheckEmailValidator checkEmailValidator;
    private final CheckNickNameValidator checkNickNameValidator;

    @InitBinder("userSaveRequestDto")
    public void saveValidatorBinder(WebDataBinder binder) {
        binder.addValidators(checkLoginIdValidator);
        binder.addValidators(checkEmailValidator);
        binder.addValidators(checkNickNameValidator);
    }

    @InitBinder("userEditRequestDto")
    public void editValidatorBinder(WebDataBinder binder) {
        binder.addValidators(checkEmailValidator);
        binder.addValidators(checkNickNameValidator);
    }

    @GetMapping("/signUp")
    public String signUpForm(Model model) {
        model.addAttribute("userSaveRequestDto", UserSaveRequestDto.builder().build());
        return "user/saveForm";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String originPath) {
        String loginFormTemplate = "user/loginForm?originPath=%s";
        return "user/loginForm";
    }

    @GetMapping("/myPage/edit")
    public String editForm(@SessionAttribute(name = SESSION_KEY, required = false) String loginId,
                           HttpServletRequest httpServletRequest,
                           Model model) {
        String signedId = (String) httpServletRequest.getSession(false).getAttribute(SESSION_KEY);
        UserGetRequestDto getRequestDto = UserMapper.INSTANCE.mapToDtoWithId(signedId);
        User user = userService.findByLoginId(getRequestDto.getLoginId());
        UserEditRequestDto userEditRequestDto = UserMapper.INSTANCE.mapToEditDto(user);
        model.addAttribute("userEditRequestDto", userEditRequestDto);

        return "user/editMyPage";
    }

    @PostMapping("/signUp")
    public String signUp(@Validated @ModelAttribute UserSaveRequestDto userSaveRequestDto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("userSaveRequestDto", userSaveRequestDto);

            Map<String, String> validators = userService.validateHandling(bindingResult);
            for (String key : validators.keySet()) {
                model.addAttribute(key, validators.get(key));
            }

            log.error("errors={}", bindingResult);
            return "user/saveForm";
        }

        UserDto userDto = UserMapper.INSTANCE.mapToDto(userSaveRequestDto, uuidGenerator, bCryptConverter);
        User signedUpUser = userService.signUp(userDto);

        redirectAttributes.addAttribute("loginId", signedUpUser.getLoginId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute UserLoginRequestDto userLoginRequestDto,
                        @RequestParam(required = false) String originPath,
                        BindingResult bindingResult,
                        HttpServletRequest httpServletRequest,
                        Model model) {
        if (bindingResult.hasErrors()) {
            log.error("errors={}", bindingResult);
            return "user/loginForm";
        }

        try {
            model.addAttribute("userLoginRequestDto", userLoginRequestDto);
            User loginedUser = userService.login(userLoginRequestDto);

            httpServletRequest.getSession().invalidate();
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute(SESSION_KEY, loginedUser.getLoginId());
            session.setMaxInactiveInterval(1800); // 30분동안 유지
            if (originPath == null) {
                originPath = "/";
            }
            return "redirect:" + originPath;

        } catch (LoginFailureException e) {
            bindingResult.rejectValue("loginId", "login.failed", "Invalid login credentials");
            log.error("Login failed for user: {}", userLoginRequestDto.getLoginId());
            return "user/loginForm";
        }
    }

    @GetMapping("/myPage")
    public String myPage(@SessionAttribute(name = SESSION_KEY, required = false) String loginId,
                         HttpServletRequest httpServletRequest,
                         Model model) {
        // URL 통해 세션이 없이 접근할 경우 예외 처리 필요
        UserGetRequestDto getRequestDto = UserMapper.INSTANCE.mapToDtoWithId(loginId);
        User user = userService.findByLoginId(getRequestDto.getLoginId());
        model.addAttribute("user", user);
        return "user/myPage";
    }

    @PostMapping("/myPage/edit")
    public String editMyPage(SessionUser user,
                             @Validated @ModelAttribute UserEditRequestDto userEditRequestDto,
                             BindingResult bindingResult,
                             HttpServletRequest httpServletRequest,
                             Model model) {
        if (bindingResult.hasErrors()) {

            model.addAttribute("userEditRequestDto", userEditRequestDto);

            Map<String, String> validators = userService.validateHandling(bindingResult);
            for (String key : validators.keySet()) {
                model.addAttribute(key, validators.get(key));
            }

            log.error("errors={}", bindingResult);
            return "user/editMyPage";
        }

        String id = (String) httpServletRequest.getSession(false).getAttribute(SESSION_KEY);
        UserGetRequestDto userGetRequestDto = UserMapper.INSTANCE.mapToDtoWithId(id);

        UserDto userDto = UserMapper.INSTANCE.mapToDtoForEdit(userEditRequestDto, user.getUserId(), bCryptConverter);
        User updatedUser = userService.update(userDto);
        model.addAttribute("user", updatedUser);
        return "user/myPage";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession(false).invalidate();
        return "redirect:/";
    }
}
