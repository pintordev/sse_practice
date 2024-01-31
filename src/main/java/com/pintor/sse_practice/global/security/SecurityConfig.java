package com.pintor.sse_practice.global.security;

import com.pintor.sse_practice.global.errors.exception_handler.ApiAuthenticationExceptionHandler;
import com.pintor.sse_practice.global.errors.exception_handler.ApiAuthorizationExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiAuthenticationExceptionHandler apiAuthenticationExceptionHandler;
    private final ApiAuthorizationExceptionHandler apiAuthorizationExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/api/**") // 아래의 모든 설정 /api/** 경로에만 적용
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/api/members").permitAll() // post:/api/members 아무나 접속 가능
                        .anyRequest().authenticated() // 그 외는 인증된 사용자만 접속 가능
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(apiAuthenticationExceptionHandler) // 인증 에러
                        .accessDeniedHandler(apiAuthorizationExceptionHandler)
                )
                .csrf(csrf -> csrf
                        .disable() // CSRF 토큰 끄기
                )
                .httpBasic(httpBasic -> httpBasic
                        .disable() // httpBasic 로그인 방식 끄기
                )
                .formLogin(formLogin -> formLogin
                        .disable() // 폼 로그인 방식 끄기
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 끄기
                )
        ;

        return http.build();
    }
}
