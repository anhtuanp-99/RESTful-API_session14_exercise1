package com.tuan.employee.config;

import com.tuan.employee.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig – cấu hình bảo mật cho ứng dụng.
 * Lý do tạo: Tùy chỉnh Spring Security cho REST API:
 * - Dùng BCrypt để mã hóa mật khẩu.
 * - Cho phép truy cập public vào /api/v1/auth/** (đăng ký, đăng nhập).
 * - Các API khác yêu cầu xác thực.
 * - Tạo AuthenticationManager để xác thực username/password.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    /**
     * PasswordEncoder sử dụng BCrypt – thuật toán băm mạnh, tự động thêm salt.
     * Lý do: Không lưu mật khẩu plaintext, bảo vệ khỏi tấn công brute-force.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager – quản lý xác thực.
     * Lý do: Cần để AuthService gọi xác thực username/password.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    /**
     * SecurityFilterChain – cấu hình bảo mật chính.
     * - Tắt CSRF (vì REST API stateless, không dùng Cookie).
     * - PermitAll cho /api/v1/auth/** (public endpoints).
     * - Các endpoint khác yêu cầu xác thực.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF cho REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll() // Public
                        .anyRequest().authenticated() // Private
                );
        return http.build();
    }
}
