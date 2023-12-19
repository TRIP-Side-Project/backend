package com.api.trip.domain.articletag.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.tag.model.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"article_id", "tag_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @Builder
    private ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }
}
