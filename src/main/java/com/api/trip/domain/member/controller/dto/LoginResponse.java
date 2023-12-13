package com.api.trip.domain.member.controller.dto;

import com.api.trip.common.security.jwt.JwtToken;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private String tokenType;
    private String accessToken;
    private String refreshToken;

    @Builder
    private LoginResponse(String tokenType, String accessToken, String refreshToken){
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse of(JwtToken jwtToken) {
        return LoginResponse.builder()
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
