package com.api.trip.domain.member.controller.dto;

import com.api.trip.domain.member.model.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class JoinRequest {

    private String email;
    private String password;
    private String nickname;
    private MultipartFile profileImg;

}
