package com.api.trip.common.security.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class OAuth2Revoke {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${custom.oauth2.kakao-revoke-uri}")
    private String kakaoRevokeUri;

    @Value("${custom.oauth2.naver-revoke-uri}")
    private String naverRevokeUri;

    @Value("${custom.oauth2.google-revoke-uri}")
    private String googleRevokeUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public void revokeKakao(String socialAccessToken) {

        log.debug("kakao revoke..");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(socialAccessToken));

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromUriString(kakaoRevokeUri)
                .build();

        restTemplate.exchange(
                uriBuilder.toString(),
                HttpMethod.POST,
                httpEntity,
                Void.class
        );

        log.debug("kakao revoke success!");
    }

    public void revokeNaver(String socialAccessToken) {
        log.debug("naver revoke..");

        UriComponents uriBuilder = UriComponentsBuilder.fromUriString(naverRevokeUri)
                .queryParam("grant_type", "delete")
                .queryParam("client_id", naverClientId)
                .queryParam("client_secret", naverClientSecret)
                .queryParam("access_token", socialAccessToken)
                .queryParam("service_provider", "naver")
                .build();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        restTemplate.exchange(
                uriBuilder.toString(),
                HttpMethod.POST,
                httpEntity,
                Void.class
        );
        log.debug("revoke naver success!");
    }

    public void revokeGoogle(String socialAccessToken) {
        log.debug("google revoke..");

        UriComponents uriBuilder = UriComponentsBuilder.fromUriString(googleRevokeUri)
                .queryParam("token", socialAccessToken)
                .build();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        restTemplate.exchange(
                uriBuilder.toString(),
                HttpMethod.POST,
                httpEntity,
                Void.class
        );
        log.debug("revoke google success!");
    }
}
