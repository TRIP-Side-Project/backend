package com.api.trip.domain.notification.controller.dto;

import com.api.trip.domain.itemtag.model.ItemTag;
import com.api.trip.domain.notification.domain.Notification;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetMyNotificationsResponse {

    private List<NotificationResponse> notifications;
    private int unread;

    public static GetMyNotificationsResponse of(List<Notification> notifications, List<ItemTag> itemTags) {
        Map<Long, List<ItemTag>> map = itemTags.stream()
                .collect(Collectors.groupingBy(itemTag -> itemTag.getItem().getId()));

        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(notification -> NotificationResponse.of(notification, map.get(notification.getItem().getId())))
                .toList();

        int unread = (int) notifications.stream()
                .filter(notification -> !notification.isRead())
                .count();

        return builder()
                .notifications(notificationResponses)
                .unread(unread)
                .build();
    }
}
