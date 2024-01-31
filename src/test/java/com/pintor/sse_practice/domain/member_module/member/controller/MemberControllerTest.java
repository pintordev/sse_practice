package com.pintor.sse_practice.domain.member_module.member.controller;

import com.pintor.sse_practice.domain.member_module.member.request.MemberRequest;
import com.pintor.sse_practice.global.controller.BaseControllerTest;
import com.pintor.sse_practice.global.response.ResCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends BaseControllerTest {

    @Transactional
    @Test
    @DisplayName("post:/api/members - created, S-01-01")
    public void signup_Created() throws Exception {

        // given
        long count = this.memberService.count();

        String username = "tester1";
        String password = "1234";
        String passwordConfirm = "1234";
        MemberRequest.SignUp request = MemberRequest.SignUp.builder()
                .username(username)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-01-01"))
                .andExpect(jsonPath("message").value(ResCode.S_01_01.getMessage()))
                .andExpect(jsonPath("data.id").value(count + 1))
                .andExpect(jsonPath("data.createDate").exists())
                .andExpect(jsonPath("data.modifyDate").exists())
                .andExpect(jsonPath("data.username").value(username))
                .andExpect(jsonPath("data.authorities[0]").value("user"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;

        assertDoesNotThrow(() -> this.memberService.getMemberById(count + 1));
    }
}