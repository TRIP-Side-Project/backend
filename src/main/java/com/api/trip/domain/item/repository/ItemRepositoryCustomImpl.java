package com.api.trip.domain.item.repository;

import com.api.trip.domain.item.model.Item;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.api.trip.domain.item.model.QItem.item;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Item> findItems(Pageable pageable, int sortCode, String search) {

        List<Item> result = jpaQueryFactory.selectFrom(item)
                //.join(item.writer).fetchJoin()
                .where(
                        eqToSearchText(search),
                        item.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderBySortCode(sortCode))
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(item.count())
                .from(item)
                .where(
                        item.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
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

    private BooleanExpression eqToSearchText(String searchText) {
        if (StringUtils.isEmpty(searchText)) return null;
        StringExpression keywordExpression = Expressions.asString("%" + searchText + "%");
        return eqToTitle(keywordExpression);
    }

    private BooleanExpression eqToTitle(StringExpression searchText) {
        return item.title.like(searchText);
    }
}
