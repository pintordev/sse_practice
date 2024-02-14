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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final ApiAuthenticationExceptionHandler apiAuthenticationExceptionHandler;
    private final ApiAuthorizationExceptionHandler apiAuthorizationExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/api/**") // 아래의 모든 설정 /api/** 경로에만 적용
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/api/members").permitAll() // post:/api/members 아무나 접속 가능
                        .requestMatchers(HttpMethod.POST, "/api/members/login").permitAll() // post:/api/members/login 아무나 접속 가능
                        .requestMatchers(HttpMethod.GET, "/api/boards/{id}").permitAll() // get:/api/boards/{id} 아무나 접속 가능
                        .requestMatchers(HttpMethod.GET, "/api/boards").permitAll() // get:/api/boards 아무나 접속 가능
                        .requestMatchers(HttpMethod.GET, "/api/comments/{id}").permitAll() // get:/api/comments/{id} 아무나 접속 가능
                        .requestMatchers(HttpMethod.GET, "/api/comments").permitAll() // get:/api/comments 아무나 접속 가능
                        .anyRequest().authenticated() // 그 외는 인증된 사용자만 접속 가능
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(this.apiAuthenticationExceptionHandler) // 인증 에러
                        .accessDeniedHandler(this.apiAuthorizationExceptionHandler)
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
                .addFilterBefore( // b filter 실행 전 a filter 실행
                        this.jwtAuthorizationFilter, // 강제 인증 할당 메서드 실행
                        UsernamePasswordAuthenticationFilter.class
                )
        ;

        return http.build();
    }
}
