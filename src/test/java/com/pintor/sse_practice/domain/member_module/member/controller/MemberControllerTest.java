package com.pintor.sse_practice.domain.member_module.member.controller;

import com.pintor.sse_practice.domain.member_module.member.request.MemberRequest;
import com.pintor.sse_practice.global.controller.BaseControllerTest;
import com.pintor.sse_practice.global.errors.exception.ApiResponseException;
import com.pintor.sse_practice.global.response.ResCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @ParameterizedTest
    @MethodSource("argsFor_signup_BadRequest_NotBlank")
    @DisplayName("post:/api/members - bad request not blank, F-01-01-01")
    public void signup_BadRequest_NotBlank(String username, String password, String passwordConfirm) throws Exception {

        // given
        long count = this.memberService.count();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-01-01"))
                .andExpect(jsonPath("message").value(ResCode.F_01_01_01.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(" "))
                .andExpect(jsonPath("_links.index").exists())
        ;

        assertThrows(ApiResponseException.class, () -> this.memberService.getMemberById(count + 1));
    }

    private static Stream<Arguments> argsFor_signup_BadRequest_NotBlank() {

        String[] usernames = {" ", "tester1"};
        String[] passwords = {" ", "1234"};
        String[] passwordConfirms = {" ", "1234"};

        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();

        for (String username : usernames)
            for (String password : passwords)
                for (String passwordConfirm : passwordConfirms)
                    if (username.isBlank() || password.isBlank() || passwordConfirm.isBlank())
                        argumentsBuilder.add(Arguments.of(username, password, passwordConfirm));

        return argumentsBuilder.build();
    }

    @Test
    @DisplayName("post:/api/members - bad request username already exist, F-01-01-02")
    public void signup_BadRequest_UsernameAlreadyExist() throws Exception {

        // given
        long count = this.memberService.count();

        String username = "user1";
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-01-02"))
                .andExpect(jsonPath("message").value(ResCode.F_01_01_02.getMessage()))
                .andExpect(jsonPath("data[0].field").value("username"))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").value("already exist"))
                .andExpect(jsonPath("data[0].defaultMessage").value("username already exists"))
                .andExpect(jsonPath("data[0].rejectedValue").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;

        assertThrows(ApiResponseException.class, () -> this.memberService.getMemberById(count + 1));
    }

    @Test
    @DisplayName("post:/api/members - bad request password not matched, F-01-01-03")
    public void signup_BadRequest_PasswordNotMatched() throws Exception {

        // given
        long count = this.memberService.count();

        String username = "tester1";
        String password = "1234";
        String passwordConfirm = "12345";
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-01-03"))
                .andExpect(jsonPath("message").value(ResCode.F_01_01_03.getMessage()))
                .andExpect(jsonPath("data[0].field").value("passwordConfirm"))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").value("not matched"))
                .andExpect(jsonPath("data[0].defaultMessage").value("passwordConfirm does not matched with password"))
                .andExpect(jsonPath("data[0].rejectedValue").value(passwordConfirm))
                .andExpect(jsonPath("_links.index").exists())
        ;

        assertThrows(ApiResponseException.class, () -> this.memberService.getMemberById(count + 1));
    }

    @Test
    @DisplayName("post:/api/members/login - ok, S-01-02")
    public void login_OK() throws Exception {

        // given
        String username = "user1";
        String password = "1234";
        MemberRequest.Login request = MemberRequest.Login.builder()
                .username(username)
                .password(password)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("code").value("S-01-02"))
                .andExpect(jsonPath("message").value(ResCode.S_01_02.getMessage()))
                .andExpect(jsonPath("data.accessToken").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @ParameterizedTest
    @MethodSource("argsFor_login_BadRequest_NotBlank")
    @DisplayName("post:/api/members/login - not blank, F-01-02-01")
    public void login_BadRequest_NotBlank(String username, String password) throws Exception {

        // given
        MemberRequest.Login request = MemberRequest.Login.builder()
                .username(username)
                .password(password)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-02-01"))
                .andExpect(jsonPath("message").value(ResCode.F_01_02_01.getMessage()))
                .andExpect(jsonPath("data[0].field").exists())
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").exists())
                .andExpect(jsonPath("data[0].defaultMessage").exists())
                .andExpect(jsonPath("data[0].rejectedValue").value(" "))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    private static Stream<Arguments> argsFor_login_BadRequest_NotBlank() {

        String[] usernames = {" ", "user1"};
        String[] passwords = {" ", "1234"};

        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();

        for (String username : usernames)
            for (String password : passwords)
                if (username.isBlank() || password.isBlank())
                    argumentsBuilder.add(Arguments.of(username, password));

        return argumentsBuilder.build();
    }

    @Test
    @DisplayName("post:/api/members/login - member not exist, F-01-02-02")
    public void login_BadRequest_MemberNotExist() throws Exception {

        // given
        String username = "tester1";
        String password = "1234";
        MemberRequest.Login request = MemberRequest.Login.builder()
                .username(username)
                .password(password)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value("NOT_FOUND"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-02-02"))
                .andExpect(jsonPath("message").value(ResCode.F_01_02_02.getMessage()))
                .andExpect(jsonPath("data[0].field").value("username"))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").value("not exist"))
                .andExpect(jsonPath("data[0].defaultMessage").value("member that has username does not exist"))
                .andExpect(jsonPath("data[0].rejectedValue").value(username))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("post:/api/members/login - password not matched, F-01-02-03")
    public void login_BadRequest_PasswordNotMatched() throws Exception {

        // given
        String username = "user1";
        String password = "12345";
        MemberRequest.Login request = MemberRequest.Login.builder()
                .username(username)
                .password(password)
                .build();

        // when
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                        .accept(MediaTypes.HAL_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("success").value("false"))
                .andExpect(jsonPath("code").value("F-01-02-03"))
                .andExpect(jsonPath("message").value(ResCode.F_01_02_03.getMessage()))
                .andExpect(jsonPath("data[0].field").value("password"))
                .andExpect(jsonPath("data[0].objectName").exists())
                .andExpect(jsonPath("data[0].code").value("not matched"))
                .andExpect(jsonPath("data[0].defaultMessage").value("password is not matched with member that has username"))
                .andExpect(jsonPath("data[0].rejectedValue").value(password))
                .andExpect(jsonPath("_links.index").exists())
        ;
    }
}