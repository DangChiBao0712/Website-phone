package poly.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import poly.edu.interceptor.AuthInterceptor;
import poly.edu.interceptor.GlobalInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    GlobalInterceptor global;

    @Autowired
    AuthInterceptor auth;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. Đăng ký Global Interceptor (Chạy cho tất cả các trang)
        registry.addInterceptor(global)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/assets/**");

        // 2. Đăng ký Auth Interceptor (Chỉ chặn các trang bảo mật)
        registry.addInterceptor(auth)
                .addPathPatterns("/admin/**", "/order/**", "/account/change-password")
                .excludePathPatterns("/security/login", "/static/**");
    }
}