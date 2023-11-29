package com.api.trip.domain.articletag.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticleTag is a Querydsl query type for ArticleTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticleTag extends EntityPathBase<ArticleTag> {

    private static final long serialVersionUID = 579797009L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticleTag articleTag = new QArticleTag("articleTag");

    public final com.api.trip.domain.article.model.QArticle article;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.api.trip.domain.tag.model.QTag tag;

    public QArticleTag(String variable) {
        this(ArticleTag.class, forVariable(variable), INITS);
    }

    public QArticleTag(Path<? extends ArticleTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticleTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticleTag(PathMetadata metadata, PathInits inits) {
        this(ArticleTag.class, metadata, inits);
    }

    public QArticleTag(Class<? extends ArticleTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new com.api.trip.domain.article.model.QArticle(forProperty("article"), inits.get("article")) : null;
        this.tag = inits.isInitialized("tag") ? new com.api.trip.domain.tag.model.QTag(forProperty("tag")) : null;
    }

}

