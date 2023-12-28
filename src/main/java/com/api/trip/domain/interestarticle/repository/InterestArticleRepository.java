package com.api.trip.domain.interestarticle.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestArticleRepository extends JpaRepository<InterestArticle, Long> {

    Optional<InterestArticle> findByMemberAndArticle(Member member, Article article);

    List<InterestArticle> findAllByMember(Member member);
}
