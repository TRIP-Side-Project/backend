package com.api.trip.domain.member.controller;

import com.api.trip.domain.email.service.EmailService;
import com.api.trip.domain.member.controller.dto.*;
import com.api.trip.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @PostMapping(value = "/join", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> join(@RequestPart JoinRequest joinRequest, @RequestPart(required = false) MultipartFile profileImg) throws IOException {

        memberService.join(joinRequest, profileImg);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    // 인증 메일 전송
    // TODO: 리팩토링 예정..
    @PostMapping("/send-email/{email}")
    public void sendAuthEmail(@PathVariable String email) {
        emailService.send(email, emailService.createEmailAuth(email));
    }

    // 이메일 인증
    @GetMapping("/auth-email/{email}/{authToken}")
    public ResponseEntity<EmailResponse> emailAndAuthToken(@PathVariable String email, @PathVariable String authToken) {
        EmailResponse emailResponse = emailService.authEmail(email, authToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("message", emailResponse.getMessage());
        headers.add("auth-email", String.valueOf(emailResponse.isAuthEmail()));

        return ResponseEntity.ok().headers(headers).body(emailResponse);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/find/password")
    public ResponseEntity<Void> sendNewPassword(@RequestBody FindPasswordRequest findPasswordRequest) {
        emailService.sendNewPassword(findPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        memberService.updatePassword(updatePasswordRequest);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteRequest deleteRequest) {
        memberService.deleteMember(deleteRequest);
        return ResponseEntity.ok().build();
    }

}
