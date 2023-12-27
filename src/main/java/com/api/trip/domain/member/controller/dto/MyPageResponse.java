package com.api.trip.domain.member.controller.dto;

import com.api.trip.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private Long likeItemCount; // 좋아요를 누른 상품의 개수


    public static MyPageResponse of(Member member, Long articleCount, Long commentCount, Long likeItemCount) {
        return MyPageResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .profileImg(member.getProfileImg())
                .createdAt(member.getCreatedAt())
                .articleCount(articleCount)
                .commentCount(commentCount)
                .likeItemCount(likeItemCount)
                .build();
    }
}
