package com.api.trip.domain.member.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailResponse {

    private String status;
    private String message;
    private boolean authEmail;

    public static EmailResponse of(boolean authEmail) {
        return EmailResponse.builder()
                .status("https://http.cat/200")
                .message("success email auth!")
                .authEmail(authEmail)
                .build();
    }
}
