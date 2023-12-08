package com.api.trip.domain.article.service;

import com.api.trip.domain.article.controller.dto.CreateArticleRequest;
import com.api.trip.domain.article.controller.dto.ReadArticleResponse;
import com.api.trip.domain.article.controller.dto.UpdateArticleRequest;
import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.article.repository.ArticleRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
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

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public Long createArticle(CreateArticleRequest request, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        Article article = Article.builder()
                .writer(member)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return articleRepository.save(article).getId();
    }

    public void updateArticle(Long articleId, UpdateArticleRequest request, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        Article article = articleRepository.findById(articleId).orElseThrow();
        if (article.getWriter() != member) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        article.modify(request.getTitle(), request.getContent());
    }

    public void deleteArticle(Long articleId, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        Article article = articleRepository.findById(articleId).orElseThrow();
        if (article.getWriter() != member) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        articleRepository.delete(article);
    }

    public ReadArticleResponse readArticle(Long articleId) {
        Article article = articleRepository.findArticle(articleId).orElseThrow();

        article.increaseViewCount();

        return ReadArticleResponse.fromEntity(article);
    }

    @Transactional(readOnly = true)
    public Page<ReadArticleResponse> getArticles(Pageable pageable, String filter) {
        return articleRepository.findArticles(pageable, filter)
                .map(ReadArticleResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<ReadArticleResponse> getMyArticles(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        List<Article> articles = articleRepository.findAllByWriterOrderByIdDesc(member);

        return articles.stream()
                .map(ReadArticleResponse::fromEntity)
                .toList();
    }
}
