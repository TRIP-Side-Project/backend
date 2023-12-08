package com.api.trip.domain.member.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private boolean emailAuth;

    @Builder
    private Member(String email, String nickname, String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = MemberRole.MEMBER;
    }

    // 이메일 인증 상태 변경 메서드
    public void emailVerifiedSuccess() {
        this.emailAuth = true;
    }
}
