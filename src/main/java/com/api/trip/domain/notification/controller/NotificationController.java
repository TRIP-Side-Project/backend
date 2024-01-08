package com.api.trip.domain.notification.controller;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;
import com.api.trip.common.sse.emitter.SseEmitterMap;
import com.api.trip.domain.notification.controller.dto.DeleteNotificationRequest;
import com.api.trip.domain.notification.controller.dto.GetMyNotificationsResponse;
import com.api.trip.domain.notification.controller.dto.ReadNotificationRequest;
import com.api.trip.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SseEmitterMap sseEmitterMap;

    @Operation(summary = "SSE 연결")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        /*
        if (email.equals("anonymous")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        */
        Long memberId = notificationService.getMemberId(email);
        SseEmitter sseEmitter = new SseEmitter(3600000L);
        sseEmitterMap.put(memberId, sseEmitter);
        sseEmitterMap.send(memberId, "connect", LocalDateTime.now());
        return ResponseEntity.ok(sseEmitter);
    }

    @Operation(summary = "알림 목록 조회")
    @GetMapping("/me")
    public ResponseEntity<GetMyNotificationsResponse> getMyNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(notificationService.getMyNotifications(email));
    }

    @Operation(summary = "알림 읽음 처리")
    @PatchMapping
    public ResponseEntity<Void> readNotification(@RequestBody ReadNotificationRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.readNotification(request, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "알림 삭제")
    @DeleteMapping
    public ResponseEntity<Void> deleteNotification(@RequestBody DeleteNotificationRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.deleteNotification(request, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "알림 목록 삭제")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.deleteMyNotifications(email);
        return ResponseEntity.ok().build();
    }
}
