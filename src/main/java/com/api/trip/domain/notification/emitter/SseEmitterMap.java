package com.api.trip.domain.notification.emitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;
import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

@Component
@Slf4j
public class SseEmitterMap {

    private final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public void put(Long memberId, SseEmitter sseEmitter) {
        sseEmitter.onCompletion(() -> remove(memberId));
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitterMap.put(memberId, sseEmitter);
        log.info("connected with {}, the number of connections is {}", memberId, sseEmitterMap.size());
    }

    public void remove(Long memberId) {
        sseEmitterMap.remove(memberId);
        log.info("disconnected with {}, the number of connections is {}", memberId, sseEmitterMap.size());
    }

    public void send(Long memberId, String eventName, Object eventData) {
        SseEmitter sseEmitter = sseEmitterMap.get(memberId);
        try {
            sseEmitter.send(
                    event()
                            .name(eventName)
                            .data(eventData)
            );
        } catch (IOException | IllegalStateException e) {
            remove(memberId);
        }
    }

    public void sendToAll(String eventName, Object eventData) {
        SseEventBuilder sseEventBuilder = event().name(eventName).data(eventData);
        sseEmitterMap.forEach((memberId, sseEmitter) -> {
            try {
                sseEmitter.send(sseEventBuilder);
            } catch (IOException | IllegalStateException e) {
                remove(memberId);
            }
        });
    }
}
