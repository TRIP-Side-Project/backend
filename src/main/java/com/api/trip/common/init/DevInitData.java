package com.api.trip.common.init;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.article.repository.ArticleRepository;
import com.api.trip.domain.comment.model.Comment;
import com.api.trip.domain.comment.repository.CommentRepository;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.interestarticle.repository.InterestArticleRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.model.SocialCode;
import com.api.trip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevInitData {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final InterestArticleRepository interestArticleRepository;

    @Value("${cloud.aws.default-image}")
    private String defaultProfileImg;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            Member member1 = memberRepository.save(createMember("trip123@test.com", "trip123", passwordEncoder.encode("trip1234")));
            Member member2 = memberRepository.save(createMember("admin@test.com", "admin", passwordEncoder.encode("trip1234")));

            Article article1 = articleRepository.save(createArticle("title1", "content1", member1));
            Article article2 = articleRepository.save(createArticle("title2", "content2", member1));
            Article article3 = articleRepository.save(createArticle("title3", "content3", member1));

            Comment comment1 = commentRepository.save(createComment(article1, member1, "댓글1", null));
            Comment comment2 = commentRepository.save(createComment(article1, member1, "댓글2", comment1));
            Comment comment3 = commentRepository.save(createComment(article1, member1, "댓글3", null));
            commentRepository.save(createComment(article1, member1, "댓글4", comment3));

            interestArticleRepository.save(createInterestArticle(member1, article1));
            interestArticleRepository.save(createInterestArticle(member1, article2));
            interestArticleRepository.save(createInterestArticle(member1, article3));
        };
    }

    private Member createMember(String email, String nickname, String password) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .profileImg(defaultProfileImg)
                .socialCode(SocialCode.NORMAL)
                .build();
    }

    private Article createArticle(String title, String content, Member member) {
        return Article.builder()
                .title(title)
                .content(content)
                .writer(member)
                .build();
    }

    private Comment createComment(Article article, Member member, String content, Comment parent) {
        return Comment.builder()
                .article(article)
                .writer(member)
                .content(content)
                .parent(parent)
                .build();
    }

    private InterestArticle createInterestArticle(Member member, Article article) {
        return InterestArticle.builder()
                .member(member)
                .article(article)
                .build();
    }
}
