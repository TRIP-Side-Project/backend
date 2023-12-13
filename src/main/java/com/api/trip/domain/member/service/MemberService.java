package com.api.trip.domain.member.service;

import com.api.trip.common.security.jwt.JwtToken;
import com.api.trip.common.security.jwt.JwtTokenProvider;
import com.api.trip.common.security.util.SecurityUtils;
import com.api.trip.domain.aws.util.MultipartFileUtils;
import com.api.trip.domain.aws.service.AmazonS3Service;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3Service amazonS3Service;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public void join(JoinRequest joinRequest, MultipartFile profileImg) throws IOException {

        // 중복된 회원이 있는지 검사
        memberRepository.findByEmail(joinRequest.getEmail()).ifPresent(it -> {
            throw new RuntimeException("이미 존재하는 회원 입니다.");
        });

        // 이메일 인증이 완료 여부 검사
        EmailAuth emailAuth = emailAuthRepository.findByEmail(joinRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일 인증 토큰 정보가 없습니다!"));

        String profileImgUrl = "";
        if (profileImg == null || profileImg.isEmpty()) {
            // 프로필 사진 데이터가 없으면 기본 이미지 세팅
            profileImgUrl = amazonS3Service.getDefaultProfileImg();
        } else {
            // 파일 변조 여부 체크
            if (!MultipartFileUtils.isPermission(profileImg.getInputStream())) {
                throw new RuntimeException("프로필 사진은 이미지 파일만 선택 가능합니다!");
            }

            // 요청 파일 이미지가 있는 경우 s3 업로드
            profileImgUrl = amazonS3Service.upload(profileImg);
        }

        Member member = Member.of(
                joinRequest.getEmail(),
                passwordEncoder.encode(joinRequest.getPassword()),
                joinRequest.getNickname(),
                profileImgUrl
        );

        member.emailVerifiedSuccess();

        // TODO: 회원 가입이 실패하는 경우 s3에 있는 파일 삭제 처리 고민 해보기
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
