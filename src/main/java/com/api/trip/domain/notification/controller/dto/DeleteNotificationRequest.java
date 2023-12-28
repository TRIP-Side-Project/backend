package com.api.trip.domain.notification.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeleteNotificationRequest {

    @NotNull(message = "itemId를 입력해 주세요.")
    private Long itemId;
}
