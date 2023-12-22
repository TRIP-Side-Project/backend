package com.api.trip.domain.notification.controller;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;
import com.api.trip.domain.member.repository.MemberRepository;
import com.api.trip.domain.notification.controller.dto.GetMyNotificationsResponse;
import com.api.trip.domain.notification.emitter.SseEmitterMap;
import com.api.trip.domain.notification.service.NotificationService;
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

    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final SseEmitterMap sseEmitterMap;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long memberId = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED))
                .getId();

        SseEmitter sseEmitter = new SseEmitter();
        sseEmitterMap.put(memberId, sseEmitter);
        sseEmitterMap.send(memberId, "connect", LocalDateTime.now());
        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping("/send-to-all")
    public void sendToAll(@RequestParam String message) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        sseEmitterMap.sendToAll("send-to-all", email + ": " + message);
    }

    @GetMapping("/me")
    public ResponseEntity<GetMyNotificationsResponse> getMyNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(notificationService.getMyNotifications(email));
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<Void> readNotification(@PathVariable Long notificationId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.readNotification(notificationId, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.deleteNotification(notificationId, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.deleteMyNotifications(email);
        return ResponseEntity.ok().build();
    }
}
