package com.api.trip.domain.member.model;


import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    MEMBER("ROLE_MEMBER");

    private String value;

    MemberRole(String value) {
        this.value = value;
    }
}
