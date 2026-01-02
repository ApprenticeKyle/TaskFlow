package org.r2learning.project.infrastructure.config;

import org.r2learning.common.infrastructure.interceptor.UserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册用户信息拦截器，拦截所有API请求
        registry.addInterceptor(new UserInfoInterceptor())
            .addPathPatterns("/api/**");
    }
}
