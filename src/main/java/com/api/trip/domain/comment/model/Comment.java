package com.api.trip.domain.comment.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.member.model.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @Builder
    private Comment(Member writer, Article article, String content, Comment parent) {
        this.writer = writer;
        this.article = article;
        this.content = content;
        this.parent = parent;
    }

    public void modify(String content) {
        this.content = content;
    }
}
