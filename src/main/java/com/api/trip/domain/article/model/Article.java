package com.api.trip.domain.article.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import com.api.trip.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    private long viewCount;

    private long likeCount;

    @Builder
    private Article(Member writer, String title, String content, long viewCount, long likeCount) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
