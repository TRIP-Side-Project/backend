package com.api.trip.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialCode {

    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google"),
    NORMAL("normal"),
    ;

    private final String type;
}
