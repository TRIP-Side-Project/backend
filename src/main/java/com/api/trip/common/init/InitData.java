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


    private final TagRepository tagRepository;

    @Bean
    public CommandLineRunner init() {
        return args -> {

            // 태그 생성
            createTag();

        };
    }


    private void createTag() {
        String[] tags = {"부산", "제주", "서울", "강원", "경남", "춘천", "목포", "여수", "안동", "경주", "대구", "대전", "전주"
        ,"눈꽃", "스키", "보드", "바다", "요트", "해양스포츠", "단풍", "골프"};

        for (String name : tags) {
            Tag tag = Tag.builder()
                    .name(name)
                    .build();

            tagRepository.save(tag);
        }
    }
}
