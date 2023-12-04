package com.api.trip.domain.member.model;

import com.api.trip.domain.member.controller.dto.JoinRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

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

    @Builder
    private Member(String email, String nickname, String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = MemberRole.MEMBER;
    }

    public static Member of(JoinRequest joinRequest, String password){
        return Member.builder()
                .email(joinRequest.getEmail())
                .nickname(joinRequest.getNickname())
                .password(password)
                .build();
    }
}
