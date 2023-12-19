package com.api.trip.domain.articletag.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.articletag.model.ArticleTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.api.trip.domain.articletag.model.QArticleTag.articleTag;
import static com.api.trip.domain.tag.model.QTag.tag;

public class ArticleTagRepositoryCustomImpl implements ArticleTagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ArticleTagRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ArticleTag> findArticleTags(Article article) {
        return jpaQueryFactory
                .selectFrom(articleTag)
                .innerJoin(articleTag.tag, tag).fetchJoin()
                .where(articleTag.article.eq(article))
                .fetch();
    }
}
