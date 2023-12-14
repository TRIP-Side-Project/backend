package com.api.trip.common.security.oauth;

import com.api.trip.common.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.debug("OAuth2User: {}", oAuth2User);

        String email = oAuth2User.getAttribute("email");

        String role = oAuth2User.getAuthorities().stream()
                .findFirst()
                .orElseThrow(IllegalAccessError::new)
                .getAuthority();

        /** 미구현
        if (isExist) {
            JwtToken jwtToken = jwtTokenProvider.createJwtToken(email, role);
            log.debug("JWT TOKEN: {} {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

            String targetUrl = UriComponentsBuilder.fromUriString("/")
                    .queryParam("accessToken", jwtToken.getAccessToken())
                    .queryParam("refreshToken", jwtToken.getRefreshToken())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // 회원이 존재하는 않는 경우 회원 가입 후 토큰 발급

            String targetUrl = UriComponentsBuilder.fromUriString("/")
                    .queryParam("email", email)
                    .queryParam("name", nickname)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
         */
    }
}
