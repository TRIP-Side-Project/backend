package com.api.trip.domain.article.repository;

import com.api.trip.domain.article.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ArticleRepositoryCustom {

    Optional<Article> findArticle(Long articleId);

    Page<Article> findArticles(Pageable pageable, int sortCode, String category, String title);

    Page<Article> findArticlesByTagName(Pageable pageable, int sortCode, String category, String tagName);
}
