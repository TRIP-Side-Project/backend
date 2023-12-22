package com.api.trip.domain.notification.repository;

import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.itemtag.model.ItemTag;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.notification.domain.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.api.trip.domain.item.model.QItem.item;
import static com.api.trip.domain.itemtag.model.QItemTag.itemTag;
import static com.api.trip.domain.notification.domain.QNotification.notification;
import static com.api.trip.domain.tag.model.QTag.tag;

public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public NotificationRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Notification> findNotifications(Member member) {
        return jpaQueryFactory
                .selectFrom(notification)
                .innerJoin(notification.item, item).fetchJoin()
                .where(notification.member.eq(member))
                .fetch();
    }

    @Override
    public List<ItemTag> findItemTags(List<Item> items) {
        return jpaQueryFactory
                .selectFrom(itemTag)
                .innerJoin(itemTag.tag, tag).fetchJoin()
                .where(itemTag.item.in(items))
                .fetch();
    }
}
