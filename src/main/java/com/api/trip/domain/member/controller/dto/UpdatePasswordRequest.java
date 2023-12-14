package com.api.trip.domain.member.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePasswordRequest {

    private final String currentPassword;
    private final String newPassword;
    private final String newPasswordConfirm;

}
