package com.api.trip.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400
    EMPTY_REFRESH_TOKEN("RefreshToken이 필요합니다.", HttpStatus.BAD_REQUEST),
    EMPTY_EMAIL("이메일이 필요합니다.", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_TYPE("유효하지 않은 파일 형식입니다.", HttpStatus.BAD_REQUEST),
    AWS_FAIL_UPLOAD("AWS S3 업로드 실패!", HttpStatus.CONFLICT),

    // 401
    LOGOUTED_TOKEN("이미 로그아웃 처리된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    SNATCH_TOKEN("Refresh Token 탈취를 감지하여 로그아웃 처리됩니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TYPE_TOKEN("Token의 타입은 Bearer입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_PERIOD_ACCESS_TOKEN("기한이 만료된 AccessToken입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_PERIOD_REFRESH_TOKEN("기한이 만료된 RefreshToken입니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_AUTHORITY("권한 정보가 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효하지 않은 AccessToken입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("유효하지 않은 RefreshToken입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_CURRENT_PASSWORD("현재 비밀번호가 일치하지 않습니다!", HttpStatus.UNAUTHORIZED),
    INVALID_NEW_PASSWORD("새 비밀번호가 일치하지 않습니다!", HttpStatus.UNAUTHORIZED),

    // 403
    FORBIDDEN_CREATE("생성 권한이 없습니다.", HttpStatus.FORBIDDEN),
    FORBIDDEN_DELETE("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),
    FORBIDDEN_UPDATE("수정 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 404
    NOT_FOUND_MEMBER("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_ITEM("상품이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_PROVIDER("지원하지 않는 소셜 로그인 플랫폼 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_EMAIL_TOKEN("이메일 인증 토큰이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    ALREADY_JOINED("이미 존재하는 회원입니다.", HttpStatus.CONFLICT),

    // 게시판 관련
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("로그인해 주세요.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ARTICLE_NOT_FOUND("존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND),
    INTEREST_ARTICLE_ALREADY_EXISTS("이미 좋아한 게시글입니다.", HttpStatus.BAD_REQUEST),
    INTEREST_ARTICLE_NOT_FOUND("좋아한 게시글이 아닙니다.", HttpStatus.NOT_FOUND),
    UPLOAD_FAILED("파일 업로드에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 알림
    NOTIFICATION_NOT_FOUND("존재하지 않는 알림입니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
