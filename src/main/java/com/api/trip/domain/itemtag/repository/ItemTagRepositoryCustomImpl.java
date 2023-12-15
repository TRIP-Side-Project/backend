package com.api.trip.domain.itemtag.repository;

import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.itemtag.model.ItemTag;
import com.api.trip.domain.tag.model.Tag;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.api.trip.domain.item.model.QItem.item;
import static com.api.trip.domain.itemtag.model.QItemTag.itemTag;

public class ItemTagRepositoryCustomImpl implements ItemTagRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ItemTagRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Item> findItemsByTags(Pageable pageable, int sortCode, List<Tag> tags) {
        List<ItemTag> result = jpaQueryFactory.selectFrom(itemTag)
                .where(
                        itemTag.tag.in(tags)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderBySortCode(sortCode))
                .fetch();


        List<Item> items = result.stream().map(ItemTag::getItem).collect(Collectors.toList());

        JPAQuery<Long> countQuery = jpaQueryFactory.select(item.count())
                .from(item)
                .where(
                        item.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(items, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] orderBySortCode(int sortCode) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();


        switch (sortCode)
        {
            case 1:
                orderSpecifiers.add(new OrderSpecifier(Order.ASC, item.id));
            case 2:
                orderSpecifiers.add(new OrderSpecifier(Order.DESC, item.likeCount));
            case 3:
                orderSpecifiers.add(new OrderSpecifier(Order.ASC, item.likeCount));

        }
        orderSpecifiers.add(new OrderSpecifier(Order.DESC, item.id));
        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
