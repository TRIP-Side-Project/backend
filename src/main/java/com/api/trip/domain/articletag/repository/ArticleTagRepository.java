package com.api.trip.domain.articletag.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.articletag.model.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long>, ArticleTagRepositoryCustom {

    @Modifying
    @Query("DELETE FROM ArticleTag at WHERE at.article = :article")
    void deleteAllByArticle(@Param("article") Article article);
}
