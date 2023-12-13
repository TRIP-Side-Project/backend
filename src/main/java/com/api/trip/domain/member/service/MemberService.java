package com.api.trip.domain.member.service;

import com.api.trip.common.security.jwt.JwtToken;
import com.api.trip.common.security.jwt.JwtTokenProvider;
import com.api.trip.common.security.util.SecurityUtils;
import com.api.trip.domain.email.model.EmailAuth;
import com.api.trip.domain.email.repository.EmailAuthRepository;
import com.api.trip.domain.member.controller.dto.DeleteRequest;
import com.api.trip.domain.member.controller.dto.JoinRequest;
import com.api.trip.domain.member.controller.dto.LoginRequest;
import com.api.trip.domain.member.controller.dto.LoginResponse;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public void join(JoinRequest joinRequest) {
        // 중복 회원 체크
        memberRepository.findByEmail(joinRequest.getEmail()).ifPresent(it -> {
            throw new RuntimeException("이미 존재하는 회원 입니다.");
        });

        EmailAuth emailAuth = emailAuthRepository.findByEmail(joinRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("토큰 정보가 없습니다!"));

        Member member = JoinRequest.of(joinRequest, passwordEncoder.encode(joinRequest.getPassword()));
        member.emailVerifiedSuccess();

        memberRepository.save(member);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String authorities = authentication.getAuthorities().stream()
                .map(a -> "ROLE_" + a.getAuthority())
                .collect(Collectors.joining(","));

        JwtToken jwtToken = jwtTokenProvider.createJwtToken(loginRequest.getEmail(), authorities);
        return LoginResponse.of(jwtToken);
    }

    public void deleteMember(DeleteRequest deleteRequest) {
        Member member = getAuthenticationMember();

        if (!passwordEncoder.matches(deleteRequest.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.deleteById(member.getId());
    }

    // 회원의 비밀번호를 메일로 전송한 임시 비밀번호로 변경
    public void changePassword(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        member.changePassword(passwordEncoder.encode(password));
    }

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("가입된 회원이 아닙니다!"));
    }

    @Transactional(readOnly = true)
    public Member getAuthenticationMember() {
        return memberRepository.findByEmail(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("가입된 회원이 아닙니다!"));
    }
}
