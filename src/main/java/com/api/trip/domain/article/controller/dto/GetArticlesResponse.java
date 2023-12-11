package com.api.trip.domain.article.controller.dto;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GetArticlesResponse {

    private int totalPages;
    private long totalElements;
    private int page;
    private boolean hasNext;
    private boolean hasPrevious;
    private int requestSize;
    private int resultSize;
    private List<ArticleDto> result;

    public static GetArticlesResponse fromEntityPage(Page<Article> articlePage) {
        Page<ArticleDto> articleDtoPage = articlePage.map(ArticleDto::fromEntity);
        return builder()
                .totalPages(articleDtoPage.getTotalPages())
                .totalElements(articleDtoPage.getTotalElements())
                .page(articleDtoPage.getNumber() + 1)
                .hasNext(articleDtoPage.hasNext())
                .hasPrevious(articleDtoPage.hasPrevious())
                .requestSize(articleDtoPage.getSize())
                .resultSize(articleDtoPage.getNumberOfElements())
                .result(articleDtoPage.getContent())
                .build();
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

        private static ArticleDto fromEntity(Article article) {
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
