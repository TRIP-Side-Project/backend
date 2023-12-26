package com.api.trip.common.naverapi;

import com.api.trip.common.naverapi.dto.ShoppingRequest;
import com.api.trip.common.naverapi.dto.ShoppingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component  //빈으로 등록
public class NaverClient {
    @Value("${custom.naver.url}")
    private String naverUrl;
    @Value("${custom.naver.clientId}")
    private String clientId;
    @Value("${custom.naver.clientSecret}")
    private String clientSecret;

    public ShoppingResponse search(ShoppingRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(naverUrl)
                .queryParams(request.map())
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ShoppingResponse> entity = new RestTemplate().exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                ShoppingResponse.class
        );

        return entity.getBody();
    }
}