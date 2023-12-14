package com.api.trip.domain.interestarticle.controller.dto;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.member.model.Member;
import lombok.Getter;

@Getter
public class CreateInterestArticleRequest {

    private Long articleId;

    public InterestArticle toEntity(Member member, Article article) {
        return InterestArticle.builder()
                .member(member)
                .article(article)
                .build();
    }
}
