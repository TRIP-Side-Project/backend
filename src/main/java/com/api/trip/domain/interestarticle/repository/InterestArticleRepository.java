package com.api.trip.domain.interestarticle.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestArticleRepository extends JpaRepository<InterestArticle, Long> {

    InterestArticle findByMemberAndArticle(Member member, Article article);
}
