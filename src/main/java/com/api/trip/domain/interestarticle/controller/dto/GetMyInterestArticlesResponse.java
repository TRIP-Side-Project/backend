package com.api.trip.domain.interestarticle.controller.dto;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetMyInterestArticlesResponse {

    private List<ArticleDto> articles;

    public static GetMyInterestArticlesResponse of(List<Article> articles) {
        List<ArticleDto> articleDtos = articles.stream()
                .map(ArticleDto::of)
                .toList();

        return new GetMyInterestArticlesResponse(articleDtos);
    }

    @Getter
    @Builder
    private static class ArticleDto {

        private Long articleId;
        private String title;
        private Long writerId;
        private String writerNickname;
        private String writerRole;
        private LocalDateTime createdAt;

        private static ArticleDto of(Article article) {
            Member writer = article.getWriter();
            return builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .writerId(writer.getId())
                    .writerNickname(writer.getNickname())
                    .writerRole(writer.getRole().name())
                    .createdAt(article.getCreatedAt())
                    .build();
        }
    }
}
