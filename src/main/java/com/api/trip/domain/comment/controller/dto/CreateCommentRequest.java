package com.api.trip.domain.comment.controller.dto;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.comment.model.Comment;
import com.api.trip.domain.member.model.Member;
import lombok.Getter;

@Getter
public class CreateCommentRequest {

    private Long articleId;
    private Long parentId;
    private String content;

    public Comment toEntity(Member writer, Article article, Comment parent) {
        return Comment.builder()
                .writer(writer)
                .article(article)
                .content(content)
                .parent(parent)
                .build();
    }
}
