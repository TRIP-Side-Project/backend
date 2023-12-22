package com.api.trip.domain.notification.controller.dto;

import com.api.trip.domain.itemtag.model.ItemTag;
import com.api.trip.domain.notification.domain.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NotificationResponse {

    private Long notificationId;
    private Long itemId;
    private String itemTitle;
    private List<String> tags;
    private boolean read;
    private LocalDateTime createdAt;

    public static NotificationResponse of(Notification notification, List<ItemTag> itemTags) {
        List<String> tagNames = itemTags.stream().map(itemTag -> itemTag.getTag().getName()).toList();
        return builder()
                .notificationId(notification.getId())
                .itemId(notification.getItem().getId())
                .itemTitle(notification.getItem().getTitle())
                .tags(tagNames)
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
