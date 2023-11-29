package com.api.trip.domain.member.model;

import com.api.trip.domain.Interestitem.model.InterestItem;
import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.articletag.model.ArticleTag;
import com.api.trip.domain.comment.domain.Comment;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.interesttag.model.InterestTag;
import com.api.trip.domain.item.model.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "writer")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<InterestTag> interestTags = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<InterestItem> interestItems = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<InterestArticle> interestArticles = new ArrayList<>();



}
