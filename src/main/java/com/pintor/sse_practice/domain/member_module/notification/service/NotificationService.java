package com.pintor.sse_practice.domain.member_module.notification.service;

import com.pintor.sse_practice.domain.member_module.member.entity.Member;
import com.pintor.sse_practice.domain.member_module.notification.repository.SseEmitterRepository;
import com.pintor.sse_practice.global.errors.exception.ApiResponseException;
import com.pintor.sse_practice.global.response.ResCode;
import com.pintor.sse_practice.global.response.ResData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter subscribe(Member member, String lastEventId) {

        String id = member.getId() + "_" + System.currentTimeMillis();

        SseEmitter sseEmitter = this.sseEmitterRepository.save(id);

        this.sendToClient(sseEmitter, id, "client has connected to server");

        return sseEmitter;
    }

    private void sendToClient(SseEmitter sseEmitter, String id, Object data) {

        try {
            sseEmitter.send(SseEmitter.event()
                    .id(id)
                    .name("connect")
                    .data(data));
        } catch (IOException exception) {
            throw new ApiResponseException(
                    ResData.of(
                            ResCode.F_04_01_01
                    )
            );
        }
    }
}
