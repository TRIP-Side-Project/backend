package com.api.trip.domain.article.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.member.model.MemberRole;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.api.trip.domain.article.model.QArticle.article;
import static com.api.trip.domain.member.model.QMember.member;

public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ArticleRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Article> findArticle(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(article)
                        .innerJoin(article.writer, member).fetchJoin()
                        .where(article.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public Page<Article> findArticles(Pageable pageable, String filter) {
        List<Article> content = jpaQueryFactory
                .selectFrom(article)
                .innerJoin(article.writer, member).fetchJoin()
                .where(eqFilter(filter))
                .orderBy(getOrderSpecifier(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(article.count())
                .from(article)
                .innerJoin(article.writer, member)
                .where(eqFilter(filter))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression eqFilter(String filter) {
        for (MemberRole role : MemberRole.values()) {
            if (role.name().equals(filter)) {
                return member.role.eq(role);
            }
        }
        return null;
    }

    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            if ("POPULAR".equals(order.getProperty())) {
                return new OrderSpecifier<>(Order.DESC, article.viewCount);
            }
        }
        return new OrderSpecifier<>(Order.DESC, article.id);
    }
}
