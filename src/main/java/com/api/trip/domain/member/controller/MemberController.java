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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "members", description = "회원 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @Operation(summary = "회원 가입", description = "이메일, 비밀번호, 닉네임, [프로필 이미지]를 입력해서 회원가입을 한다.")
    @PostMapping(value = "/join")
    public ResponseEntity<Void> join(@ModelAttribute @Valid JoinRequest joinRequest) throws IOException {

        memberService.join(joinRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인", description = "이메일, 비밀번호를 입력해서 로그인한다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "마이페이지", description = "마이페이지 화면으로 이동한다.")
    @GetMapping("/me")
    public ResponseEntity<MyPageResponse> myPage() {
        MyPageResponse myPageResponse = memberService.myPage();
        return ResponseEntity.ok().body(myPageResponse);
    }


    // 인증 메일 전송
    @Operation(summary = "인증 메일 전송", description = "인증 링크를 포함한 메일을 발송한다.")
    @PostMapping("/send-email/{email}")
    public void sendAuthEmail(@PathVariable String email) {
        emailService.send(email);
    }

    // 이메일 인증
    @Operation(summary = "이메일 인증", description = "인증 메일이 유효한지 검사하고 인증을 처리한다.")
    @GetMapping("/auth-email/{email}/{authToken}")
    public ResponseEntity<EmailResponse> emailAndAuthToken(@PathVariable String email, @PathVariable String authToken) {
        boolean isAuth = emailService.authEmail(email, authToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "success email auth!");
        headers.add("auth-email", String.valueOf(isAuth));

        return ResponseEntity.ok().headers(headers).body(EmailResponse.of(isAuth));
    }

    @Operation(summary = "비밀번호 찾기", description = "임시 비밀번호를 발급한다.")
    @PreAuthorize("isAnonymous()")
    @PostMapping("/find/password")
    public ResponseEntity<Void> sendNewPassword(@RequestBody FindPasswordRequest findPasswordRequest) {
        emailService.sendNewPassword(findPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 수정", description = "프로필 이미지, 닉네임, 관심 태그를 수정한다.")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me")
    public ResponseEntity<Void> updateProfile(@ModelAttribute UpdateProfileRequest updateProfileRequest) throws IOException {
        memberService.updateProfile(updateProfileRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호를 변경한다.")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        memberService.updatePassword(updatePasswordRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "회원 탈퇴", description = "[일반 회원]의 탈퇴 처리를 진행한다.")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@RequestBody DeleteRequest deleteRequest) {
        memberService.deleteMember(deleteRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "소셜 회원 탈퇴", description = "[소셜 회원]의 탈퇴 처리를 진행한다.")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/social/me")
    public ResponseEntity<Void> deleteSocialMember() {
        memberService.deleteSocialMember();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃", description = "현재 계정을 로그아웃 처리한다.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<String> logoutMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok().body(memberService.logout(username));
    }

    @Operation(summary = "refreshToken 재발급", description = "만료된 refreshToken을 재발급한다.")
    @GetMapping("/rotate")
    public JwtToken rotateToken(HttpServletRequest request){
        String refreshToken = JwtTokenUtils.extractBearerToken(request.getHeader("refreshToken"));

        if(refreshToken.isBlank())
            throw new BadRequestException(ErrorCode.EMPTY_REFRESH_TOKEN);


        return memberService.rotateToken(refreshToken);
    }
}
