package com.api.trip.domain.member.service;

import com.api.trip.common.security.JwtToken;
import com.api.trip.common.security.JwtTokenProvider;
import com.api.trip.domain.member.controller.dto.JoinRequest;
import com.api.trip.domain.member.controller.dto.LoginRequest;
import com.api.trip.domain.member.controller.dto.LoginResponse;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;


    public void join(JoinRequest joinRequest) {
        Member member = Member.of(joinRequest, passwordEncoder.encode(joinRequest.getPassword()));
        memberRepository.save(member);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String authorities = authentication.getAuthorities().stream()
                .map(a -> "ROLE_" + a.getAuthority())
                .collect(Collectors.joining(","));
        // 인증 성공
        // 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.createJwtToken(authentication.getName(), authorities);



        return LoginResponse.of(jwtToken);
    }
}
