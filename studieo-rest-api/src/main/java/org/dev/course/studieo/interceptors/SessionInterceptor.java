package org.dev.course.studieo.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {
    private final static String LOGIN_URL_TEMPLATE = "/users/login?originPath=%s";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession(false) == null) {
            String originPath = request.getRequestURI();
            response.sendRedirect(format(LOGIN_URL_TEMPLATE, originPath));
            return false;
        }
        return true;
    }
}
