package com.api.trip.domain.item.repository;

import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.member.model.QMember;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.api.trip.domain.item.model.QItem.item;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Item> findItems(Pageable pageable) {


        List<Item> result = jpaQueryFactory.selectFrom(item)
                .join(item.writer).fetchJoin()
                .where(
                        item.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                //.orderBy()
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(item.count())
                .from(item)
                .where(
                        item.isDeleted.eq(false)
                );

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }


}
