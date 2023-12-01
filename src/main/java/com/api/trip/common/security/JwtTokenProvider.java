package com.api.trip.common.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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

    public JwtToken createJwtToken(String email, String authorities) {

        Date expirationTime = new Date(System.currentTimeMillis() + accessExpirationTime);
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", authorities); //

        String accessToken = Jwts.builder()
                .setClaims(claims) // 아이디, 권한정보
                .setExpiration(expirationTime) // 만료기간
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return new JwtToken(accessToken, "refreshToken");
    }

    public boolean validateAccessToken(String accessToken) {
        try {
                parseToken(accessToken);
                return true;
            }
        catch (final JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    private Claims parseToken(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
    }

    public Authentication getAuthenticationByAccessToken(String accessToken) {

        Claims claims = parseToken(accessToken);

        /**
         * if (claims.get("roles") == null)
         * 권한정보 없을 떄 예외처리
         */


        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }
}
