package com.api.trip.domain.article.model;

import com.api.trip.domain.articletag.model.ArticleTag;
import com.api.trip.domain.comment.domain.Comment;
import com.api.trip.domain.itemtag.model.ItemTag;
import com.api.trip.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer viewCount;

    @Enumerated(EnumType.STRING)
    private ArticleType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    @OneToMany(mappedBy = "article")
    private List<ArticleTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

}
