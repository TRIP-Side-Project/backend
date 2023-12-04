package com.api.trip.common.security;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtToken {

    private final String accessToken;
    private final String refreshToken;

}
