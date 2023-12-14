package com.api.trip.domain.article.controller.dto;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.member.model.Member;
import lombok.Getter;

@Getter
public class CreateArticleRequest {

    private String title;
    private String content;

    public Article toEntity(Member writer) {
        return Article.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .build();
    }
}
