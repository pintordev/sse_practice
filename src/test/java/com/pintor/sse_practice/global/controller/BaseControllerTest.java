package com.pintor.sse_practice.global.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pintor.sse_practice.domain.member_module.member.request.MemberRequest;
import com.pintor.sse_practice.domain.member_module.member.service.MemberService;
import com.pintor.sse_practice.global.util.AppConfig;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Disabled // 테스트 미 간주
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected MemberService memberService;

    protected String getAccessToken(String username, String password) {
        return "Bearer "
                + this.memberService.login(
                MemberRequest.Login.of(username, password),
                AppConfig.getMockErrors()
        );
    }
}
