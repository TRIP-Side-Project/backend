package com.api.trip.domain.article.controller.dto;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReadArticleResponse {

    private Long articleId;
    private String title;
    private Long writerId;
    private String writerNickname;
    private String writerRole;
    private String content;
    private long viewCount;
    private LocalDateTime createdAt;
    private Long interestArticleId;

    public static ReadArticleResponse of(Article article, InterestArticle interestArticle) {
        Member writer = article.getWriter();
        return builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .writerId(writer.getId())
                .writerNickname(writer.getNickname())
                .writerRole(writer.getRole().name())
                .content(article.getContent())
                .viewCount(article.getViewCount())
                .createdAt(article.getCreatedAt())
                .interestArticleId(interestArticle != null ? interestArticle.getId() : null)
                .build();
    }
}
