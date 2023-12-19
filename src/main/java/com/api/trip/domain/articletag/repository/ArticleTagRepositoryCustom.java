package com.api.trip.domain.articletag.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.articletag.model.ArticleTag;

import java.util.List;

public interface ArticleTagRepositoryCustom {

    List<ArticleTag> findArticleTags(Article article);
}
