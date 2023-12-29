package com.api.trip.common.sse.emitter;

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

    private final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public void put(String email, SseEmitter sseEmitter) {
        sseEmitter.onCompletion(() -> remove(email));
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitterMap.put(email, sseEmitter);
        log.info("connected with {}, the number of connections is {}", email, sseEmitterMap.size());
    }

    public void remove(String email) {
        sseEmitterMap.remove(email);
        log.info("disconnected with {}, the number of connections is {}", email, sseEmitterMap.size());
    }

    public void send(String email, String eventName, Object eventData) {
        SseEmitter sseEmitter = sseEmitterMap.get(email);
        try {
            sseEmitter.send(
                    event()
                            .name(eventName)
                            .data(eventData)
            );
        } catch (IOException | IllegalStateException e) {
            remove(email);
        }
    }

    public void sendToAll(String eventName, Object eventData) {
        SseEventBuilder sseEventBuilder = event().name(eventName).data(eventData);
        sseEmitterMap.forEach((email, sseEmitter) -> {
            try {
                sseEmitter.send(sseEventBuilder);
            } catch (IOException | IllegalStateException e) {
                remove(email);
            }
        });
    }
}
