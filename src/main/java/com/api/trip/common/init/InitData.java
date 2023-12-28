package com.api.trip.common.init;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.article.repository.ArticleRepository;
import com.api.trip.domain.comment.model.Comment;
import com.api.trip.domain.comment.repository.CommentRepository;
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

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class InitData {

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

            // 태그 생성
            createTag();

        };
    }


    private void createTag() {
        String[] tags = {"부산", "제주", "서울", "요트", "경주", "눈꽃여행", "기차"};

        for (String name : tags) {
            Tag tag = Tag.builder()
                    .name(name)
                    .build();

            tagRepository.save(tag);
        }
    }
}
