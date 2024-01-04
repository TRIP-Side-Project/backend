package com.api.trip.domain.member.service;

import com.api.trip.common.exception.ErrorCode;
import com.api.trip.common.exception.custom_exception.DuplicateException;
import com.api.trip.common.exception.custom_exception.InvalidException;
import com.api.trip.common.exception.custom_exception.NotFoundException;
import com.api.trip.common.exception.custom_exception.NotMatchException;
import com.api.trip.common.security.jwt.JwtToken;
import com.api.trip.common.security.jwt.JwtTokenProvider;
import com.api.trip.common.security.oauth.OAuth2Revoke;
import com.api.trip.common.security.util.JwtTokenUtils;
import com.api.trip.common.security.util.SecurityUtils;
import com.api.trip.domain.article.repository.ArticleRepository;
import com.api.trip.domain.aws.util.MultipartFileUtils;
import com.api.trip.domain.aws.service.AmazonS3Service;
import com.api.trip.domain.comment.repository.CommentRepository;
import com.api.trip.domain.email.repository.EmailRedisRepository;
import com.api.trip.domain.interestitem.repository.InterestItemRepository;
import com.api.trip.domain.interesttag.service.InterestTagService;
import com.api.trip.domain.member.controller.dto.*;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.model.SocialCode;
import com.api.trip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.trip.common.exception.ErrorCode.SNATCH_TOKEN;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final EmailRedisRepository emailRedisRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final InterestItemRepository interestItemRepository;

    private final OAuth2Revoke oAuth2Revoke;
    private final AmazonS3Service amazonS3Service;
    private final InterestTagService interestTagService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${cloud.aws.default-image}")
    private String defaultProfileImgUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 회원가입
    public void join(JoinRequest joinRequest) throws IOException {

        // 중복된 회원이 있는지 확인
        memberRepository.findByEmail(joinRequest.getEmail()).ifPresent(it -> {
            throw new DuplicateException(ErrorCode.ALREADY_JOINED);
        });

        if (!emailRedisRepository.existToken(joinRequest.getEmail())) {
            throw new NotFoundException(ErrorCode.EXPIRED_EMAIL_TOKEN);
        }

        MultipartFile profileImg = joinRequest.getProfileImg();

        String profileImgUrl = "";
        if (profileImg == null || profileImg.isEmpty()) {
            // 프로필 사진 데이터가 없으면 기본 이미지 세팅
            profileImgUrl = amazonS3Service.getDefaultProfileImg();
        } else {
            // 파일 변조 여부 체크
            if (!MultipartFileUtils.isPermission(profileImg.getInputStream())) {
                throw new InvalidException(ErrorCode.INVALID_IMAGE_TYPE);
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

        memberRepository.save(member);
        emailRedisRepository.deleteToken(joinRequest.getEmail());
    }

    // 로그인
    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String authorities = authentication.getAuthorities().stream()
                .map(a -> "ROLE_" + a.getAuthority())
                .collect(Collectors.joining(","));

        JwtToken jwtToken = jwtTokenProvider.createJwtToken(loginRequest.getEmail(), authorities);
        Member member = getMemberByEmail(loginRequest.getEmail());
        return LoginResponse.of(jwtToken, member);
    }

    public MyPageResponse myPage() {
        Member member = getAuthenticationMember();

        Long articleCount = articleRepository.countByWriter_Id(member.getId());
        Long commentCount = commentRepository.countByWriter_Id(member.getId());
        Long likeItemCount = interestItemRepository.countByMember_Id(member.getId());

        long[] counts = {articleCount, commentCount, likeItemCount};
        List<String> tags = interestTagService.getInterestTag(member);

        return MyPageResponse.of(member, counts, tags);
    }

    // 비밀번호 변경
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        Member member = getAuthenticationMember();

        if (!passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), member.getPassword())) {
            throw new NotMatchException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getNewPasswordConfirm())) {
            throw new NotMatchException(ErrorCode.INVALID_NEW_PASSWORD);
        }

        member.changePassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
    }

    // 회원 정보 수정
    public void updateProfile(UpdateProfileRequest updateProfileRequest) throws IOException {
        Member member = getAuthenticationMember();

        MultipartFile profileImg = updateProfileRequest.getProfileImg();

        String profileImgUrl = "";
        // 프로필 사진 데이터가 넘어오는 경우에만 업로드 후 변경
        if (profileImg != null && !profileImg.isEmpty()) {
            if (!MultipartFileUtils.isPermission(profileImg.getInputStream())) {
                throw new InvalidException(ErrorCode.INVALID_IMAGE_TYPE);
            }

            // 이전 프로필 이미지가 기본 이미지가 아니라면 s3에서 삭제 후 업로드
            String prevProfileImgUrl = member.getProfileImg();
            if (!prevProfileImgUrl.contains(defaultProfileImgUrl)) {
                String key = prevProfileImgUrl.split(bucket + "/")[1];
                log.debug("s3 delete key: {}", key);

                amazonS3Service.delete(key);
                log.debug("deleting previous profile-img: {}", prevProfileImgUrl);
            }

            // 요청 파일 이미지가 있는 경우 s3 업로드
            profileImgUrl = amazonS3Service.upload(profileImg);
            member.changeProfileImg(profileImgUrl);
        }

        // 관심 태그 저장
        interestTagService.createTag(member, updateProfileRequest.getTags());
        member.changeProfile(updateProfileRequest);
    }



    // 일반 회원 탈퇴
    public void deleteMember(DeleteRequest deleteRequest) {
        Member member = getAuthenticationMember();

        if (!passwordEncoder.matches(deleteRequest.getPassword(), member.getPassword())) {
            throw new NotMatchException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        memberRepository.deleteById(member.getId());
    }

    // 소셜 회원 삭제
    public void deleteSocialMember() {
        Member member = getAuthenticationMember();

        SocialCode socialCode = member.getSocialCode();
        String socialAccessToken = member.getSocialAccessToken();

        log.debug("socialCode: {}", socialCode);
        // 각 플랫폼 별로 연결 끊기
        switch (socialCode) {
            case KAKAO -> oAuth2Revoke.revokeKakao(socialAccessToken);
            case NAVER -> oAuth2Revoke.revokeNaver(socialAccessToken);
            case GOOGLE ->  oAuth2Revoke.revokeGoogle(socialAccessToken);
        }

        memberRepository.deleteById(member.getId());
    }

    // 회원의 비밀번호를 임시 비밀번호로 업데이트
    public void changePassword(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        member.changePassword(passwordEncoder.encode(password));
    }


    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Transactional(readOnly = true)
    public Member getAuthenticationMember() {
        return memberRepository.findByEmail(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
    }

    /**
     * @Description
     *  1. 요청으로 들어온 RT검증 + 로그인 여부 체킹
     *  2. 현재 RT와 요청으로 들어온 RT비교
     *  3. 다르다면 토큰 탈취로 간주 후 로그아웃 처리 + 예외 처리
     */
    public JwtToken rotateToken(String requestRefreshToken) {
        Authentication authentication = validateAndGetAuthentication(requestRefreshToken);
        String userEmail = authentication.getName();

        checkLogin(userEmail);

        String currentRefreshToken = jwtTokenUtils.getRefreshToken(userEmail);

        if(isSnatch(requestRefreshToken, currentRefreshToken) == true) {
            logout(authentication.getName());
            throw new InvalidException(SNATCH_TOKEN);
        }

        return jwtTokenProvider.refreshJwtToken(authentication);
    }

    private boolean isSnatch(String requestRefreshToken, String currentRefreshToken) {
        return !currentRefreshToken.equals(requestRefreshToken);
    }

    private void checkLogin(String email) {
        jwtTokenProvider.checkLogin(email);
    }

    private Authentication validateAndGetAuthentication(String requestRefreshToken){
        return  jwtTokenProvider.getAuthenticationByRefreshToken(requestRefreshToken);
    }

    public String logout(String email){
        jwtTokenUtils.deleteRefreshToken(email);
        return "로그아웃 처리 되었습니다.";
    }
}
