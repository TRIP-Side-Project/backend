package com.api.trip.domain.member.controller;

import com.api.trip.domain.member.controller.dto.JoinRequest;
import com.api.trip.domain.member.controller.dto.LoginRequest;
import com.api.trip.domain.member.controller.dto.LoginResponse;
import com.api.trip.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinMember(@RequestBody JoinRequest joinRequest){
        memberService.join(joinRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public  ResponseEntity<LoginResponse> loginMember(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = memberService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }
}
