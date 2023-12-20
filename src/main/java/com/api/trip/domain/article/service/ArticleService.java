package com.api.trip.domain.article.service;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;
import com.api.trip.domain.article.controller.dto.*;
import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.article.repository.ArticleRepository;
import com.api.trip.domain.article.util.UrlExtractor;
import com.api.trip.domain.articlefile.repository.ArticleFileRepository;
import com.api.trip.domain.articletag.model.ArticleTag;
import com.api.trip.domain.articletag.repository.ArticleTagRepository;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.interestarticle.repository.InterestArticleRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import com.api.trip.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ArticleTagRepository articleTagRepository;
    private final TagRepository tagRepository;
    private final ArticleFileRepository articleFileRepository;
    private final InterestArticleRepository interestArticleRepository;

    public Long createArticle(CreateArticleRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        final Article article = Article.builder()
                .writer(member)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        articleRepository.save(article);

        tagRepository.findByNameIn(request.getTags())
                .forEach(tag -> {
                    ArticleTag articleTag = ArticleTag.builder()
                            .article(article)
                            .tag(tag)
                            .build();
                    articleTagRepository.save(articleTag);
                });

        List<String> urls = UrlExtractor.extractAll(request.getContent());
        if (urls.size() > 0) {
            articleFileRepository.setArticleWhereArticleNullAndUrlIn(article, urls);
        }

        return article.getId();
    }

    public void updateArticle(Long articleId, UpdateArticleRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        if (article.getWriter() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        articleTagRepository.deleteAllByArticle(article);
        tagRepository.findByNameIn(request.getTags())
                .forEach(tag -> {
                    ArticleTag articleTag = ArticleTag.builder()
                            .article(article)
                            .tag(tag)
                            .build();
                    articleTagRepository.save(articleTag);
                });

        List<String> urls = UrlExtractor.extractAll(request.getContent());
        if (urls.size() > 0) {
            articleFileRepository.setArticleNullWhereArticleAndUrlNotIn(article, urls);
            articleFileRepository.setArticleWhereArticleNullAndUrlIn(article, urls);
        }

        article.modify(request.getTitle(), request.getContent());
    }

    public void deleteArticle(Long articleId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        if (article.getWriter() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        articleTagRepository.deleteAllByArticle(article);

        articleFileRepository.setArticleNullWhereArticle(article);

        articleRepository.delete(article);
    }

    public ReadArticleResponse readArticle(Long articleId, String email) {
        Article article = articleRepository.findArticle(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        List<ArticleTag> articleTags = articleTagRepository.findArticleTags(article);

        InterestArticle interestArticle = null;
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member != null) {
            interestArticle = interestArticleRepository.findByMemberAndArticle(member, article).orElse(null);
        }

        articleRepository.increaseViewCount(article);

        return ReadArticleResponse.of(article, articleTags, interestArticle);
    }

    @Transactional(readOnly = true)
    public GetArticlesResponse getArticles(Pageable pageable, int sortCode, String category, String title, String tagName) {
        Page<Article> articlePage = null;

        if (tagName == null) {
            articlePage = articleRepository.findArticles(pageable, sortCode, category, title);
        }

        if (tagName != null) {
            articlePage = articleRepository.findArticlesByTagName(pageable, sortCode, category, tagName);
        }

        return GetArticlesResponse.of(articlePage);
    }

    @Transactional(readOnly = true)
    public GetMyArticlesResponse getMyArticles(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        List<Article> articles = articleRepository.findAllByWriterOrderByIdDesc(member);

        return GetMyArticlesResponse.of(articles);
    }
}
