package com.api.trip.common.security;


import lombok.Getter;

@Getter
public class JwtToken {
    private String accessToken;
    private String refreshToken;

    public JwtToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
