package com.api.trip.domain.article.repository;

import com.api.trip.domain.article.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepositoryCustom {

    Optional<Article> findArticle(@Param("articleId") Long articleId);

    Page<Article> findArticles(Pageable pageable, String filter);
}
