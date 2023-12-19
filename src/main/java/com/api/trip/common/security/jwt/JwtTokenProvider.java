package com.api.trip.common.security.jwt;


import com.api.trip.common.exception.ErrorCode;
import com.api.trip.common.exception.custom_exception.InvalidException;
import com.api.trip.common.security.util.JwtTokenUtils;
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

    private final JwtTokenUtils jwtTokenUtils;

    @Value("${custom.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${custom.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    private final Key key;

    @Autowired
    public JwtTokenProvider(@Value("${custom.jwt.token.secret}") String secretKey, JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken createJwtToken(String email, String authorities) {

        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", authorities);


        String accessToken = createAccessToken(claims, new Date(now.getTime() + accessExpirationTime));
        String refreshToken = createRefreshToken(claims, new Date(now.getTime() + refreshExpirationTime));


        return new JwtToken(accessToken, refreshToken);
    }
    public JwtToken refreshJwtToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        JwtToken jwtToken = createJwtToken(authentication.getName(), authorities);

        jwtTokenUtils.updateRefreshToken(authentication.getName(), jwtToken.getRefreshToken());
        return jwtToken;
    }


    private String createAccessToken(Claims claims, Date expiredDate) {
        return  Jwts.builder()
                .setClaims(claims) // 아이디, 권한정보
                .setIssuedAt(new Date(System.currentTimeMillis())) // 생성일 설정
                .setExpiration(expiredDate) // 만료일 설정
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    private String createRefreshToken(Claims claims, Date expiredDate) {
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        jwtTokenUtils.setRefreshToken(claims.getSubject(), refreshToken);

        return refreshToken;
    }

    /**
     * @Description
     * AccessToken 검증 + Return 인증객체
     */
    public Authentication getAuthenticationByAccessToken(String accessToken) {

        Claims claims = validateAccessToken(accessToken);

        if (claims.get("roles") == null){
            throw new InvalidException(ErrorCode.EMPTY_AUTHORITY);
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * @Description
     * RefreshToken 검증 + Return 인증객체
     */
    public Authentication getAuthenticationByRefreshToken(String refreshToken){
        Claims claims = validateRefreshToken(refreshToken);

        if (claims.get("roles") == null){
            throw new InvalidException(ErrorCode.EMPTY_AUTHORITY);
        }
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    /**
     * @Description
     * 토큰의 만료여부와 유효성에 대해 검증합니다.
     */
    private Claims validateAccessToken(String accessToken) {
        try {
            return parseToken(accessToken);

        } catch (ExpiredJwtException e) {
            throw new InvalidException(ErrorCode.EXPIRED_PERIOD_ACCESS_TOKEN);
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
    private Claims validateRefreshToken(String refreshToken) {
        try {
            return parseToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new InvalidException(ErrorCode.EXPIRED_PERIOD_REFRESH_TOKEN);
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private Claims parseToken(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void checkLogin(String email) {
        if (jwtTokenUtils.isLogin(email) == false)
            throw new InvalidException(ErrorCode.LOGOUTED_TOKEN);
    }
}
