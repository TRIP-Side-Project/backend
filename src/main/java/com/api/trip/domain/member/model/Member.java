package com.api.trip.domain.member.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import com.api.trip.domain.member.controller.dto.JoinRequest;
import com.api.trip.domain.member.controller.dto.UpdateProfileRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private String intro; // 자기 소개

    @Column(nullable = false)
    private String profileImg;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private boolean emailAuth;

    @Builder
    private Member(String email, String password, String nickname, String profileImg){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.role = MemberRole.MEMBER;
    }

    public static Member of(String email, String password, String nickname, String profileImg) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileImg(profileImg)
                .build();
    }

    // 이메일 인증 상태 변경 메서드
    public void emailVerifiedSuccess() {
        this.emailAuth = true;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeProfileImg(String profileImgUrl) {
        this.profileImg = profileImgUrl;
    }

    public void changeProfile(UpdateProfileRequest updateProfileRequest) {
        this.nickname = updateProfileRequest.getNickname();
        this.intro = updateProfileRequest.getIntro();
    }
}
