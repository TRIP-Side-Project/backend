package com.api.trip.domain.articlefile.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.articlefile.model.ArticleFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, Long> {

    @Modifying
    @Query("UPDATE ArticleFile af SET af.article = :article WHERE af.article IS NULL AND af.url IN :urls")
    void setArticleWhereArticleNullAndUrlIn(@Param("article") Article article, @Param("urls") List<String> urls);

    @Modifying
    @Query("UPDATE ArticleFile af SET af.article = null WHERE af.article = :article AND af.url NOT IN :urls")
    void setArticleNullWhereArticleAndUrlNotIn(@Param("article") Article article, @Param("urls") List<String> urls);

    @Modifying
    @Query("UPDATE ArticleFile af SET af.article = null WHERE af.article = :article")
    void setArticleNullWhereArticle(@Param("article") Article article);
}
