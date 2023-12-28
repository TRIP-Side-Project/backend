package com.api.trip.domain.member.controller.dto;

import com.api.trip.common.security.jwt.JwtToken;
import com.api.trip.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final Long memberId;
    private final String tokenType;
    private final String accessToken;
    private final String refreshToken;
    private final String profileImgUrl;

    @Builder
    private LoginResponse(String accessToken, String refreshToken, Long memberId, String profileImgUrl){
        this.memberId = memberId;
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.profileImgUrl = profileImgUrl;
    }

    public static LoginResponse of(JwtToken jwtToken, Member member) {
        return LoginResponse.builder()
                .memberId(member.getId())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .profileImgUrl(member.getProfileImg())
                .build();
    }
}
