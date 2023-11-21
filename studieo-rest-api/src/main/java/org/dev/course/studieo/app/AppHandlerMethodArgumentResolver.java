package org.dev.course.studieo.app;

import lombok.RequiredArgsConstructor;
import org.dev.course.studieo.domain.user.entity.User;
import org.dev.course.studieo.domain.user.entity.vo.SessionUser;
import org.dev.course.studieo.domain.user.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.dev.course.studieo.constants.Constants.SESSION_KEY;

@Component
@RequiredArgsConstructor
public class AppHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return SessionUser.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String loginId = (String) webRequest.getAttribute(SESSION_KEY, WebRequest.SCOPE_SESSION);
        User user = userService.findByLoginId(loginId);
        return SessionUser.builder().userId(user.getUserId())
                                    .loginId(loginId)
                                    .build();
    }
}
