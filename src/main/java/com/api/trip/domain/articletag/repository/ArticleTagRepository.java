package com.api.trip.domain.articletag.repository;

import com.api.trip.domain.articletag.model.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
}
