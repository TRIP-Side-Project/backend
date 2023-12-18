package com.api.trip.domain.articlefile.repository;

import com.api.trip.domain.articlefile.model.ArticleFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, Long> {
}
