package com.api.trip.common.security.jwt;

import com.api.trip.common.security.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String accessToken = JwtTokenUtils.extractBearerToken(request.getHeader("accessToken"));



        if (!request.getRequestURI().equals("/api/members/rotate") && accessToken != null) { // 토큰 재발급의 요청이 아니면서 accessToken이 존재할 때

            // 토큰이 유효한 경우 and 로그인 상태
            Authentication authentication = jwtTokenProvider.getAuthenticationByAccessToken(accessToken);
            jwtTokenProvider.checkLogin(authentication.getName());

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/api/members/join", "/api/members/login",
                "/api/members/send-email", "/api/members/auth-email"
        };

        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
