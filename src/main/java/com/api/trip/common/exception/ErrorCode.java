package com.api.trip.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400
    EMPTY_REFRESH_TOKEN("RefreshToken이 필요합니다.", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_TYPE("유효하지 않은 파일 형식입니다.", HttpStatus.BAD_REQUEST),

    // 401
    LOGOUTED_TOKEN("이미 로그아웃 처리된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    SNATCH_TOKEN("Refresh Token 탈취를 감지하여 로그아웃 처리됩니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TYPE_TOKEN("Token의 타입은 Bearer입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_PERIOD_ACCESS_TOKEN("기한이 만료된 AccessToken입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_PERIOD_REFRESH_TOKEN("기한이 만료된 RefreshToken입니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_AUTHORITY("권한 정보가 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효하지 않은 AccessToken입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("유효하지 않은 RefreshToken입니다.", HttpStatus.UNAUTHORIZED),

    // 404
    NOT_FOUND_MEMBER("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_PROVIDER("지원하지 않는 소셜 로그인 플랫폼 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_EMAIL_TOKEN("이메일 인증 토큰이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    ALREADY_JOINED("이미 존재하는 회원입니다.", HttpStatus.CONFLICT),
    ;


    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }


}
