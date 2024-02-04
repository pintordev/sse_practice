package com.pintor.sse_practice.domain.member_module.notification.controller;

import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.domain.member_module.member.service.MemberService;
import com.pintor.sse_practice.domain.member_module.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequestMapping(value = "/api/notifications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final MemberService memberService;
    private final NotificationService notificationService;

    @GetMapping(value = "/connect", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> sseConnect(@AuthenticationPrincipal User user,
                                                 @RequestHeader(value = "Last-Event-Id", required = false, defaultValue = "") String lastEventId) {

        log.info(lastEventId);

        Member member = this.memberService.getMemberByUsername(user.getUsername());
        SseEmitter sseEmitter = this.notificationService.subscribe(member, lastEventId);

        return ResponseEntity.ok()
                .body(sseEmitter);
    }
}
