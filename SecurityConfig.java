package poly.edu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Tắt CSRF để các form nhập liệu không bị lỗi 403
            .csrf(csrf -> csrf.disable())
            
            // 2. Cho phép truy cập TẤT CẢ các đường dẫn (Để code Interceptor của anh tự xử lý)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() 
            );

        return http.build();
    }
}