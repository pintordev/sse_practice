package com.pintor.sse_practice.domain.member_module.notification.controller;

import com.pintor.sse_practice.global.controller.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("get:/api/notifications/connect - ok, S-04-01")
    public void sseConnect_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        String accessToken = this.getAccessToken(username, password);

        Long memberId = this.memberService.getMemberByUsername(username).getId();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(get("/api/notifications/connect")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.TEXT_EVENT_STREAM)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id:%s_".formatted(memberId))))
                .andExpect(content().string(containsString("event:connect")))
                .andExpect(content().string(containsString("data:client has connected to server")))
        ;


    }

}