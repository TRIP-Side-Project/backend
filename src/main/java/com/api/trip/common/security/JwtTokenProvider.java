package com.api.trip.common.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    /**
     * JWT 생성, 검증, 변환
     */

    @Value("${custom.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    private final Key key;

    @Autowired
    public JwtTokenProvider(@Value("${custom.jwt.token.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken createJwtToken(String username, String authorities) {

        Date expirationTime = new Date(System.currentTimeMillis() + accessExpirationTime);
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", authorities); //

        String accessToken = Jwts.builder()
                .setClaims(claims) // 아이디, 권한정보
                .setExpiration(expirationTime) // 만료기간
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return new JwtToken(accessToken, "refreshToken");
    }
}
