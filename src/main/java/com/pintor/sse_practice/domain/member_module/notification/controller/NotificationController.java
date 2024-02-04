package com.pintor.sse_practice.domain.member_module.notification.controller;

import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.domain.member_module.member.service.MemberService;
import com.pintor.sse_practice.domain.member_module.notification.service.NotificationService;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import com.pintor.sse_practice.global.util.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(value = "/api/notifications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final MemberService memberService;
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity subscribe(@AuthenticationPrincipal User user,
                                    @RequestHeader(value = "lastEventId", required = false, defaultValue = "") String lastEventId) {

        Member member = this.memberService.getMemberByUsername(user.getUsername());
        SseEmitter sseEmitter = this.notificationService.subscribe(member, lastEventId);

        ResData resData = ResData.of(
                ResCode.S_04_01,
                sseEmitter,
                linkTo(this.getClass()).slash("subscribe")
        );
        resData.add(Link.of(AppConfig.getBaseURL() + "/notifications/subscribe").withRel("profile"));
        return ResponseEntity.created(linkTo(this.getClass()).slash("subscribe").toUri())
                .body(resData);
    }
}
