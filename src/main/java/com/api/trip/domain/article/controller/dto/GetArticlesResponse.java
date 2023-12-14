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

    private List<ArticleDto> articles;
    private Pagination pagination;

    public static GetArticlesResponse of(Page<Article> articlePage) {
        Page<ArticleDto> articleDtoPage = articlePage.map(ArticleDto::of);
        return builder()
                .articles(articleDtoPage.getContent())
                .pagination(Pagination.of(articleDtoPage))
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
        private long viewCount;
        private long likeCount;
        private LocalDateTime createdAt;

        private static ArticleDto of(Article article) {
            Member writer = article.getWriter();
            return builder()
                    .articleId(article.getId())
                    .title(article.getTitle())
                    .writerId(writer.getId())
                    .writerNickname(writer.getNickname())
                    .writerRole(writer.getRole().name())
                    .viewCount(article.getViewCount())
                    .likeCount(article.getLikeCount())
                    .createdAt(article.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    private static class Pagination {

        private int totalPages;
        private long totalElements;
        private int page;
        private boolean hasNext;
        private boolean hasPrevious;
        private int requestSize;
        private int articleSize;

        private static Pagination of(Page<ArticleDto> articleDtoPage) {
            return builder()
                    .totalPages(articleDtoPage.getTotalPages())
                    .totalElements(articleDtoPage.getTotalElements())
                    .page(articleDtoPage.getNumber() + 1)
                    .hasNext(articleDtoPage.hasNext())
                    .hasPrevious(articleDtoPage.hasPrevious())
                    .requestSize(articleDtoPage.getSize())
                    .articleSize(articleDtoPage.getNumberOfElements())
                    .build();
        }
    }
}
