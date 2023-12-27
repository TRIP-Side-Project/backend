package com.api.trip.domain.member.controller;

import com.api.trip.common.exception.ErrorCode;
import com.api.trip.common.exception.custom_exception.BadRequestException;
import com.api.trip.common.security.jwt.JwtToken;
import com.api.trip.common.security.util.JwtTokenUtils;
import com.api.trip.domain.email.service.EmailService;
import com.api.trip.domain.member.controller.dto.*;
import com.api.trip.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "members", description = "회원 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @PostMapping(value = "/join")
    public ResponseEntity<Void> join(@ModelAttribute @Valid JoinRequest joinRequest) throws IOException {

        memberService.join(joinRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<MyPageResponse> myPage() {
        MyPageResponse myPageResponse = memberService.myPage();
        return ResponseEntity.ok().body(myPageResponse);
    }


    // 인증 메일 전송
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
    @PatchMapping("/me")
    public ResponseEntity<Void> updateProfile(@ModelAttribute UpdateProfileRequest updateProfileRequest) throws IOException {
        memberService.updateProfile(updateProfileRequest);
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

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logoutMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok().body(memberService.logout(username));
    }

    @GetMapping("/rotate")
    public JwtToken rotateToken(HttpServletRequest request){
        String refreshToken = JwtTokenUtils.extractBearerToken(request.getHeader("refreshToken"));

        if(refreshToken.isBlank())
            throw new BadRequestException(ErrorCode.EMPTY_REFRESH_TOKEN);


        return memberService.rotateToken(refreshToken);
    }
}
