package com.surakshasetu.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private StudentSessionInterceptor studentSessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(studentSessionInterceptor)
                .addPathPatterns("/student-portal/**")
                .excludePathPatterns(
                        "/student-portal/login",
                        "/student-portal/forgot-password",
                        "/student-portal/verify-otp",
                        "/student-portal/reset-password",
                        "/student-portal/logout"
                );
    }
}