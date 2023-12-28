package com.api.trip.common.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SseNotificationResponse {

    private Long itemId;
    private List<String> tagNames;
}
