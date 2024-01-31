package com.pintor.sse_practice.domain.member_module.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class MemberRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignUp {

        @NotBlank
        private String username;

        @NotBlank
        private String password;

        @NotBlank
        private String passwordConfirm;
    }
}
