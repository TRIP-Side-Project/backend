package com.api.trip.domain.notification.repository;

import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.itemtag.model.ItemTag;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.notification.domain.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> findNotifications(Member member);

    List<ItemTag> findItemTags(List<Item> items);
}
