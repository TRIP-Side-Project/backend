package com.api.trip.domain.member.controller.dto;

import com.api.trip.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MyPageResponse {

    private String email;
    private String nickname;
    private String intro;
    private String profileImg;
    private LocalDateTime createdAt;
    private Long articleCount;
    private Long commentCount;
    private Long likeItemCount;
    private List<String> tags;

    public static MyPageResponse of(Member member, long[] counts, List<String> tags) {
        return MyPageResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .profileImg(member.getProfileImg())
                .createdAt(member.getCreatedAt())
                .articleCount(counts[0])
                .commentCount(counts[1])
                .likeItemCount(counts[2])
                .tags(tags)
                .build();
    }
}
