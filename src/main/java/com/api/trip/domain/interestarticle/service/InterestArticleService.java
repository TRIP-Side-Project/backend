package com.api.trip.domain.interestarticle.service;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;
import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.article.repository.ArticleRepository;
import com.api.trip.domain.interestarticle.controller.dto.CreateInterestArticleRequest;
import com.api.trip.domain.interestarticle.controller.dto.GetMyInterestArticlesResponse;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.interestarticle.repository.InterestArticleRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestArticleService {

    private final MemberRepository memberRepository;
    private final InterestArticleRepository interestArticleRepository;
    private final ArticleRepository articleRepository;

    public Long createInterestArticle(CreateInterestArticleRequest request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        interestArticleRepository.findByMemberAndArticle(member, article)
                .ifPresent(interestArticle -> {
                    throw new CustomException(ErrorCode.INTEREST_ARTICLE_ALREADY_EXISTS);
                });

        articleRepository.increaseLikeCount(article);

        return interestArticleRepository.save(
                        InterestArticle.builder()
                                .member(member)
                                .article(article)
                                .build()
                )
                .getId();
    }

    public void deleteInterestArticle(Long interestArticleId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        InterestArticle interestArticle = interestArticleRepository.findById(interestArticleId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTEREST_ARTICLE_NOT_FOUND));

        if (interestArticle.getMember() != member) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        articleRepository.decreaseLikeCount(interestArticle.getArticle());

        interestArticleRepository.delete(interestArticle);
    }

    public GetMyInterestArticlesResponse getMyInterestArticles(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        List<InterestArticle> interestArticles = interestArticleRepository.findAllByMember(member);

        List<Article> articles = articleRepository.findAllById(
                interestArticles
                        .stream()
                        .map(interestArticle -> interestArticle.getArticle().getId())
                        .toList()
        );

        return GetMyInterestArticlesResponse.of(articles);
    }
}
