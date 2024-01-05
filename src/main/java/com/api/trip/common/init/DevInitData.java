package com.api.trip.common.init;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.article.repository.ArticleRepository;
import com.api.trip.domain.comment.model.Comment;
import com.api.trip.domain.comment.repository.CommentRepository;
import com.api.trip.domain.interestarticle.model.InterestArticle;
import com.api.trip.domain.interestarticle.repository.InterestArticleRepository;
import com.api.trip.domain.interestitem.model.InterestItem;
import com.api.trip.domain.interestitem.repository.InterestItemRepository;
import com.api.trip.domain.interesttag.model.InterestTag;
import com.api.trip.domain.interesttag.respository.InterestTagRepository;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.item.repository.ItemRepository;
import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.model.SocialCode;
import com.api.trip.domain.member.repository.MemberRepository;
import com.api.trip.domain.tag.model.Tag;
import com.api.trip.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class DevInitData {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final InterestItemRepository interestItemRepository;
    private final TagRepository tagRepository;
    private final InterestTagRepository interestTagRepository;

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

            // 2번 회원이 상품 2개 추가
            Item item1 = itemRepository.save(createItem(1L, "서울/경기도 투어", member2));
            Item item2 = itemRepository.save(createItem(2L, "부산 해산물 투어", member2));

            // 1번 회원이 상품 2개 좋아요
            interestItemRepository.save(createInterestItem(item1, member1));
            interestItemRepository.save(createInterestItem(item2, member1));

            // 태그 생성
            createTag();

            // 관심 태그 설정
            saveInterestTag(member1, "부산");
            saveInterestTag(member1, "요트");
            saveInterestTag(member1, "경주");
        };
    }

    private void saveInterestTag(Member member, String name) {
        InterestTag interestTag1 = InterestTag.builder()
                .tag(tagRepository.findByName(name))
                .member(member)
                .build();

        interestTagRepository.save(interestTag1);
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

    private Item createItem(Long productId, String title, Member member) {
        return Item.builder()
                .productId(productId)
                .title(title)
                .shopName("trip")
                .buyUrl("buyUrl")
                .maxPrice(10000)
                .minPrice(100)
                .imageUrl("imageUrl")
                .writer(member)
                .build();
    }

    private InterestItem createInterestItem(Item item, Member member) {
        return InterestItem.builder()
                .item(item)
                .member(member)
                .build();
    }

    private void createTag() {
        String[] tags = {"부산", "요트", "경주", "눈꽃여행", "기차"};

        for (String name : tags) {
            Tag tag = Tag.builder()
                    .name(name)
                    .build();

            tagRepository.save(tag);
        }
    }
}
