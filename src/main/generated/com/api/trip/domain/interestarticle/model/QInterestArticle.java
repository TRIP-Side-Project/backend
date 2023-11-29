package com.api.trip.domain.interestarticle.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInterestArticle is a Querydsl query type for InterestArticle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterestArticle extends EntityPathBase<InterestArticle> {

    private static final long serialVersionUID = -1900813917L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInterestArticle interestArticle = new QInterestArticle("interestArticle");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.api.trip.domain.item.model.QItem item;

    public final com.api.trip.domain.member.model.QMember member;

    public QInterestArticle(String variable) {
        this(InterestArticle.class, forVariable(variable), INITS);
    }

    public QInterestArticle(Path<? extends InterestArticle> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInterestArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInterestArticle(PathMetadata metadata, PathInits inits) {
        this(InterestArticle.class, metadata, inits);
    }

    public QInterestArticle(Class<? extends InterestArticle> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new com.api.trip.domain.item.model.QItem(forProperty("item"), inits.get("item")) : null;
        this.member = inits.isInitialized("member") ? new com.api.trip.domain.member.model.QMember(forProperty("member")) : null;
    }

}

