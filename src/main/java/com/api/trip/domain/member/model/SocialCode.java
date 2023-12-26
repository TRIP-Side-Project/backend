package com.api.trip.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialCode {

    SOCIAL("SOCIAL_LOGIN"), NORMAL("NORMAL_LOGIN"),;

    private final String type;
}
