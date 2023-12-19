package com.api.trip.domain.article.service;

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
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleTagRepository articleTagRepository;
    private final TagRepository tagRepository;
    private final InterestArticleRepository interestArticleRepository;
    private final ArticleFileRepository articleFileRepository;

    public Long createArticle(CreateArticleRequest request, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        final Article article = request.toEntity(member);

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
        Member member = memberRepository.findByEmail(email).orElseThrow();

        Article article = articleRepository.findById(articleId).orElseThrow();
        if (article.getWriter() != member) {
            throw new RuntimeException("수정 권한이 없습니다.");
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
        Member member = memberRepository.findByEmail(email).orElseThrow();

        Article article = articleRepository.findById(articleId).orElseThrow();
        if (article.getWriter() != member) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        articleTagRepository.deleteAllByArticle(article);

        articleFileRepository.setArticleNullWhereArticle(article);

        articleRepository.delete(article);
    }

    public ReadArticleResponse readArticle(Long articleId, String email) {
        Article article = articleRepository.findArticle(articleId).orElseThrow();

        List<ArticleTag> articleTags = articleTagRepository.findArticleTags(article);

        InterestArticle interestArticle = null;
        if (!Objects.equals(email, "anonymousUser")) {
            Member member = memberRepository.findByEmail(email).orElseThrow();
            interestArticle = interestArticleRepository.findByMemberAndArticle(member, article);
        }

        articleRepository.increaseViewCount(article);

        return ReadArticleResponse.of(article, articleTags, interestArticle);
    }

    @Transactional(readOnly = true)
    public GetArticlesResponse getArticles(Pageable pageable, String filter) {
        Page<Article> articlePage = articleRepository.findArticles(pageable, filter);

        return GetArticlesResponse.of(articlePage);
    }

    @Transactional(readOnly = true)
    public GetMyArticlesResponse getMyArticles(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        List<Article> articles = articleRepository.findAllByWriterOrderByIdDesc(member);

        return GetMyArticlesResponse.of(articles);
    }
}
