package com.api.trip.domain.member.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1500597617L;

    public static final QMember member = new QMember("member1");

    public final ListPath<com.api.trip.domain.article.model.Article, com.api.trip.domain.article.model.QArticle> articles = this.<com.api.trip.domain.article.model.Article, com.api.trip.domain.article.model.QArticle>createList("articles", com.api.trip.domain.article.model.Article.class, com.api.trip.domain.article.model.QArticle.class, PathInits.DIRECT2);

    public final ListPath<com.api.trip.domain.comment.domain.Comment, com.api.trip.domain.comment.domain.QComment> comments = this.<com.api.trip.domain.comment.domain.Comment, com.api.trip.domain.comment.domain.QComment>createList("comments", com.api.trip.domain.comment.domain.Comment.class, com.api.trip.domain.comment.domain.QComment.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.api.trip.domain.interestarticle.model.InterestArticle, com.api.trip.domain.interestarticle.model.QInterestArticle> interestArticles = this.<com.api.trip.domain.interestarticle.model.InterestArticle, com.api.trip.domain.interestarticle.model.QInterestArticle>createList("interestArticles", com.api.trip.domain.interestarticle.model.InterestArticle.class, com.api.trip.domain.interestarticle.model.QInterestArticle.class, PathInits.DIRECT2);

    public final ListPath<com.api.trip.domain.Interestitem.model.InterestItem, com.api.trip.domain.Interestitem.model.QInterestItem> interestItems = this.<com.api.trip.domain.Interestitem.model.InterestItem, com.api.trip.domain.Interestitem.model.QInterestItem>createList("interestItems", com.api.trip.domain.Interestitem.model.InterestItem.class, com.api.trip.domain.Interestitem.model.QInterestItem.class, PathInits.DIRECT2);

    public final ListPath<com.api.trip.domain.interesttag.model.InterestTag, com.api.trip.domain.interesttag.model.QInterestTag> interestTags = this.<com.api.trip.domain.interesttag.model.InterestTag, com.api.trip.domain.interesttag.model.QInterestTag>createList("interestTags", com.api.trip.domain.interesttag.model.InterestTag.class, com.api.trip.domain.interesttag.model.QInterestTag.class, PathInits.DIRECT2);

    public final ListPath<com.api.trip.domain.item.model.Item, com.api.trip.domain.item.model.QItem> items = this.<com.api.trip.domain.item.model.Item, com.api.trip.domain.item.model.QItem>createList("items", com.api.trip.domain.item.model.Item.class, com.api.trip.domain.item.model.QItem.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final EnumPath<MemberRole> role = createEnum("role", MemberRole.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

