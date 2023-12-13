package com.api.trip.domain.interestarticle.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "article_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InterestArticle extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Builder
    private InterestArticle(Member member, Article article) {
        this.member = member;
        this.article = article;
    }
}
