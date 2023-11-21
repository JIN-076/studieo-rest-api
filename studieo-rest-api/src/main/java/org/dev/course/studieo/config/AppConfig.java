package org.dev.course.studieo.config;

import lombok.RequiredArgsConstructor;
import org.dev.course.studieo.app.AppHandlerMethodArgumentResolver;
import org.dev.course.studieo.interceptors.SessionInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {
    private final SessionInterceptor sessionInterceptor;
    private final AppHandlerMethodArgumentResolver appHandlerMethodArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/users/login/**", "/users/signUp/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(appHandlerMethodArgumentResolver);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
