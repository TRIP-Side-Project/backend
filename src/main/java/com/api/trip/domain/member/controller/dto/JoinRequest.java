package com.api.trip.domain.member.controller.dto;

import com.api.trip.domain.member.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class JoinRequest {

    private String email;
    private String password;
    private String nickname;
}
