package com.api.trip.domain.member.controller.dto;

import com.api.trip.domain.member.model.Member;
import com.api.trip.domain.member.model.SocialCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MyPageResponse {

    private SocialCode socialCode;
    private String email;
    private String nickname;
    private String intro;
    private String profileImg;
    private LocalDateTime createdAt;
    private Long articleCount;
    private Long likeArticleCount;
    private Long commentCount;
    private Long likeItemCount;
    private List<String> tags;

    public static MyPageResponse of(Member member, long[] counts, List<String> tags) {
        return MyPageResponse.builder()
                .socialCode(member.getSocialCode())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .profileImg(member.getProfileImg())
                .createdAt(member.getCreatedAt())
                .articleCount(counts[0])
                .likeArticleCount(counts[1])
                .commentCount(counts[2])
                .likeItemCount(counts[3])
                .tags(tags)
                .build();
    }
}
