package com.api.trip.domain.articlefile.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import com.api.trip.domain.article.model.Article;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleFile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Builder
    private ArticleFile(String url, Article article) {
        this.url = url;
        this.article = article;
    }
}
