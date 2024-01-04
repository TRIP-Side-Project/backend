package com.api.trip.common.security.oauth;

import com.api.trip.common.exception.ErrorCode;
import com.api.trip.common.exception.custom_exception.NotFoundException;
import com.api.trip.common.security.jwt.JwtToken;
import com.api.trip.common.security.jwt.JwtTokenProvider;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.model.SocialCode;
import com.api.trip.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String provider = oAuth2User.getAttribute("provider");

        // KAKAO_user123@naver.com
        String email = provider + "_" + oAuth2User.getAttribute("email");
        Optional<Member> findMember = memberRepository.findByEmail(email);

        // 회원이 아닌 경우에 회원 가입 진행
        Member member = null;
        if (findMember.isEmpty()) {
            // KAKAO_user123
            String name = provider + "_" + oAuth2User.getAttribute("name");
            String picture = oAuth2User.getAttribute("picture");
            SocialCode socialCode = oAuth2User.getAttribute("socialCode");
            String socialAccessToken = oAuth2User.getAttribute("accessToken");

            member = Member.of(email, "", name, picture, socialCode, socialAccessToken);
            memberRepository.save(member);
        } else {
            member = findMember.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        }

        // OAuth2User 객체에서 권한 가져옴
        JwtToken jwtToken = jwtTokenProvider.createJwtToken(member.getEmail(), member.getRole().getValue());

        String targetUrl = UriComponentsBuilder.fromUriString("https://triptrip.site/home")
                .queryParam("accessToken", jwtToken.getAccessToken())
                .queryParam("refreshToken", jwtToken.getRefreshToken())
                .queryParam("memberId", String.valueOf(member.getId()))
                .queryParam("profileImgUrl", member.getProfileImg())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);


    }

    private static String createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(60 * 60 * 6)
                .build()
                .toString();
    }

}
