package com.api.trip.domain.article.repository;

import com.api.trip.domain.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
