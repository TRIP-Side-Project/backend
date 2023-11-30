package com.api.trip.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    ADMIN("ROLE_ADMIN"),
    EDITOR("ROLE_EDITOR"),
    MEMBER("ROLE_MEMBER");

    private final String value;
}
