package com.api.trip.domain.article.controller.dto;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadArticleResponse {

    private Long articleId;
    private String title;
    private Long writerId;
    private String writerNickName;
    private String writerRole;
    private String content;
    private long viewCount;

    public static ReadArticleResponse fromEntity(Article article) {
        Member writer = article.getWriter();
        return builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .writerId(writer.getId())
                .writerNickName(writer.getNickname())
                .writerRole(writer.getRole().name())
                .content(article.getContent())
                .viewCount(article.getViewCount())
                .build();
    }
}
