package com.api.trip.domain.article.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = -570351081L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticle article = new QArticle("article");

    public final ListPath<com.api.trip.domain.comment.domain.Comment, com.api.trip.domain.comment.domain.QComment> comments = this.<com.api.trip.domain.comment.domain.Comment, com.api.trip.domain.comment.domain.QComment>createList("comments", com.api.trip.domain.comment.domain.Comment.class, com.api.trip.domain.comment.domain.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.api.trip.domain.articletag.model.ArticleTag, com.api.trip.domain.articletag.model.QArticleTag> tags = this.<com.api.trip.domain.articletag.model.ArticleTag, com.api.trip.domain.articletag.model.QArticleTag>createList("tags", com.api.trip.domain.articletag.model.ArticleTag.class, com.api.trip.domain.articletag.model.QArticleTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final EnumPath<ArticleType> type = createEnum("type", ArticleType.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public final com.api.trip.domain.member.model.QMember writer;

    public QArticle(String variable) {
        this(Article.class, forVariable(variable), INITS);
    }

    public QArticle(Path<? extends Article> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticle(PathMetadata metadata, PathInits inits) {
        this(Article.class, metadata, inits);
    }

    public QArticle(Class<? extends Article> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new com.api.trip.domain.member.model.QMember(forProperty("writer")) : null;
    }

}

