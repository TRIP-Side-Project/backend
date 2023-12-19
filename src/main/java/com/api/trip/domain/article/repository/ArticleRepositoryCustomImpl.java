package com.api.trip.domain.article.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.articletag.model.ArticleTag;
import com.api.trip.domain.member.model.MemberRole;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.api.trip.domain.article.model.QArticle.article;
import static com.api.trip.domain.articletag.model.QArticleTag.articleTag;
import static com.api.trip.domain.member.model.QMember.member;
import static com.api.trip.domain.tag.model.QTag.tag;

public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ArticleRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Article> findArticle(Long articleId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(article)
                        .innerJoin(article.writer, member).fetchJoin()
                        .where(article.id.eq(articleId))
                        .fetchOne()
        );
    }

    @Override
    public Page<Article> findArticles(Pageable pageable, int sortCode, String category, String title) {
        List<Article> content = jpaQueryFactory
                .select(article)
                .from(article)
                .innerJoin(article.writer, member).fetchJoin()
                .where(eqCategory(category), containsTitle(title))
                .orderBy(getOrderSpecifiers(sortCode))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(article.count())
                .from(article)
                .innerJoin(article.writer, member)
                .where(eqCategory(category), containsTitle(title))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Article> findArticlesByTagName(Pageable pageable, int sortCode, String category, String tagName) {
        List<ArticleTag> articleTags = jpaQueryFactory
                .select(articleTag)
                .from(articleTag)
                .innerJoin(articleTag.tag, tag)
                .where(tag.name.eq(tagName))
                .fetch();

        List<Article> articles = articleTags.stream().map(ArticleTag::getArticle).toList();

        List<Article> content = jpaQueryFactory
                .select(article)
                .from(article)
                .innerJoin(article.writer, member).fetchJoin()
                .where(eqCategory(category), article.in(articles))
                .orderBy(getOrderSpecifiers(sortCode))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(article.count())
                .from(article)
                .innerJoin(article.writer, member)
                .where(eqCategory(category), article.in(articles))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression eqCategory(String category) {
        for (MemberRole role : MemberRole.values()) {
            if (role.name().equals(category)) {
                return member.role.eq(role);
            }
        }
        return null;
    }

    private BooleanExpression containsTitle(String title) {
        return title != null ? article.title.contains(title) : null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(int sortCode) {
        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();
        switch (sortCode) {
            case 1 -> orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, article.id));
            case 2 -> orderSpecifierList.add(new OrderSpecifier<>(Order.DESC, article.likeCount));
            case 3 -> orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, article.likeCount));
        }
        orderSpecifierList.add(new OrderSpecifier<>(Order.DESC, article.id));
        return orderSpecifierList.toArray(OrderSpecifier[]::new);
    }
}
