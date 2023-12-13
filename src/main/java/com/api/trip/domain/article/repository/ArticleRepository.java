package com.api.trip.domain.article.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    List<Article> findAllByWriterOrderByIdDesc(Member writer);

    @Modifying
    @Query("UPDATE Article a SET a.likeCount = a.likeCount + 1 WHERE a = :article")
    void increaseLikeCount(@Param("article") Article article);

    @Modifying
    @Query("UPDATE Article a SET a.likeCount = a.likeCount - 1 WHERE a = :article")
    void decreaseLikeCount(@Param("article") Article article);
}
