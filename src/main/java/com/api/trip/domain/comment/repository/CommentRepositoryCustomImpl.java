package com.api.trip.domain.comment.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.comment.model.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.api.trip.domain.comment.model.QComment.comment;
import static com.api.trip.domain.member.model.QMember.member;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CommentRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Comment> findComments(Article article) {
        return jpaQueryFactory
                .selectFrom(comment)
                .innerJoin(comment.writer, member).fetchJoin()
                .where(comment.article.eq(article))
                .fetch();
    }
}
