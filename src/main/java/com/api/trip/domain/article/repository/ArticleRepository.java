package com.api.trip.domain.article.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    List<Article> findAllByWriterOrderByIdDesc(Member writer);
}
