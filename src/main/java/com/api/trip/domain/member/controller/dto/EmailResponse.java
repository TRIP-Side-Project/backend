package com.api.trip.domain.member.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailResponse {

    private String message;
    private boolean authEmail;

    public static EmailResponse of(boolean authEmail) {
        return EmailResponse.builder()
                .message("success email auth!")
                .authEmail(authEmail)
                .build();
    }
}
