package com.pintor.sse_practice.domain.member_module.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class SseEmitterRepository {

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final Long defaultTimeOut = 60L * 1000 * 60;

    public SseEmitter save(String id) {
        SseEmitter sseEmitter = new SseEmitter(this.defaultTimeOut);
        this.sseEmitters.put(id, sseEmitter);
        return sseEmitter;
    }
}
