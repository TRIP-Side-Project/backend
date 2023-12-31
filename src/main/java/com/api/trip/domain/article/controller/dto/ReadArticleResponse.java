package com.api.trip.domain.article.controller.dto;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.articletag.model.ArticleTag;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReadArticleResponse {

    private Long articleId;
    private String title;
    private Long writerId;
    private String writerNickname;
    private String writerRole;
    private String writerProfileImg;
    private String writerIntro;
    private List<String> tags;
    private String content;
    private long viewCount;
    private long likeCount;
    private LocalDateTime createdAt;
    private Long interestArticleId;

    public static ReadArticleResponse of(Article article, List<ArticleTag> articleTags, InterestArticle interestArticle) {
        Member writer = article.getWriter();
        return builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .writerId(writer.getId())
                .writerNickname(writer.getNickname())
                .writerIntro(writer.getIntro())
                .writerProfileImg(writer.getProfileImg())
                .writerRole(writer.getRole().name())
                .tags(articleTags.stream().map(articleTag -> articleTag.getTag().getName()).toList())
                .content(article.getContent())
                .viewCount(article.getViewCount() + 1)
                .likeCount(article.getLikeCount())
                .createdAt(article.getCreatedAt())
                .interestArticleId(interestArticle != null ? interestArticle.getId() : null)
                .build();
    }
}
