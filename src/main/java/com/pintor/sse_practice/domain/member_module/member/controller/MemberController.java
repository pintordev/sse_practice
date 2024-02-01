package com.pintor.sse_practice.domain.member_module.member.controller;


import com.pintor.sse_practice.domain.member_module.member.dto.MemberCreatedDto;
import com.pintor.sse_practice.domain.member_module.member.dto.MemberTokenDto;
import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.domain.member_module.member.request.MemberRequest;
import com.pintor.sse_practice.domain.member_module.member.service.MemberService;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(value = "/api/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity signup(@Valid @RequestBody MemberRequest.SignUp request, Errors errors) {

        Member member = this.memberService.signup(request, false, errors);

        ResData resData = ResData.of(
                ResCode.S_01_01,
                MemberCreatedDto.of(member),
                linkTo(this.getClass()).slash("login")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/member/signup").withRel("profile"));
        return ResponseEntity.created(linkTo(this.getClass()).slash("login").toUri())
                .body(resData);
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@Valid @RequestBody MemberRequest.Login request, Errors errors) {

        String accessToken = this.memberService.login(request, errors);

        ResData resData = ResData.of(
                ResCode.S_01_02,
                MemberTokenDto.of(accessToken)
        );
        resData.add(Link.of(AppConfig.getIndexURL()).withSelfRel());
        resData.add(Link.of(AppConfig.getBaseURL() + "/member/login").withRel("profile"));
        return ResponseEntity.ok()
                .body(resData);
    }
}
