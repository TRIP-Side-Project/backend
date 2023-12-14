package com.api.trip.domain.email.model;

import com.api.trip.common.auditing.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth extends BaseTimeEntity {

    private static final Long MAX_EXPIRE_TIME = 5L; // 링크 유효 기간 5분

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // 가입 이메일

    private String authToken; // 인증 토큰 (UUID)

    private boolean expired; // 토큰 사용 여부 (인증 완료: true, 인증 미완료: false)

    private LocalDateTime expireDate; // 인증 토큰 만료일 (발급 날짜 + 5분)

    @Builder
    public EmailAuth(String email, String authToken, boolean expired) {
        this.email = email;
        this.authToken = authToken;
        this.expired = expired;
        this.expireDate = LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME);
    }

    public void useToken() {
        this.expired = true;
    }

}
