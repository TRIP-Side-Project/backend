package com.api.trip.domain.member.controller;

import com.api.trip.common.security.util.SecurityUtils;
import com.api.trip.domain.email.service.EmailService;
import com.api.trip.domain.member.controller.dto.*;
import com.api.trip.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinMember(@RequestBody JoinRequest joinRequest) {
        memberService.join(joinRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginMember(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    // 인증 메일 전송
    // TODO: 리팩토링 예정..
    @PostMapping("/send-email/{email}")
    public void sendAuthEmail(@PathVariable String email) {
        emailService.send(email, emailService.createEmailAuth(email));
    }

    @GetMapping("/auth-email/{email}/{authToken}")
    public ResponseEntity<EmailResponse> emailAndAuthToken(@PathVariable String email, @PathVariable String authToken) {
        return ResponseEntity.ok().body(emailService.authEmail(email, authToken));
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/find/password")
    public ResponseEntity<Void> sendNewPassword(@RequestBody FindPasswordRequest findPasswordRequest) {
        emailService.sendNewPassword(findPasswordRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteRequest deleteRequest) {
        String email = SecurityUtils.getCurrentUsername();
        memberService.deleteMember(email, deleteRequest.getPassword());

        return ResponseEntity.ok().build();
    }

}
