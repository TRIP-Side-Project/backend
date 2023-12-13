package com.api.trip.domain.member.controller.dto;

import com.api.trip.domain.member.model.Member;
import lombok.Getter;

@Getter
public class JoinRequest {

    private String email;
    private String password;
    private String nickname;

    public static Member of(JoinRequest joinRequest, String password){
        return Member.builder()
                .email(joinRequest.getEmail())
                .nickname(joinRequest.getNickname())
                .password(password)
                .build();
    }
}
