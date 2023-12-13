package com.api.trip.domain.interestarticle.service;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.article.repository.ArticleRepository;
import com.api.trip.domain.interestarticle.controller.dto.CreateInterestArticleRequest;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.interestarticle.repository.InterestArticleRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestArticleService {

    private final InterestArticleRepository interestArticleRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public Long createInterestArticle(CreateInterestArticleRequest request, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        Article article = articleRepository.findById(request.getArticleId()).orElseThrow();

        InterestArticle interestArticle = interestArticleRepository.findByMemberAndArticle(member, article);
        if (interestArticle != null) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        interestArticle = request.toEntity(member, article);

        return interestArticleRepository.save(interestArticle).getId();
    }

    public void deleteInterestArticle(Long interestArticleId, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        InterestArticle interestArticle = interestArticleRepository.findById(interestArticleId).orElseThrow();
        if (interestArticle.getMember() != member) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        interestArticleRepository.delete(interestArticle);
    }
}
