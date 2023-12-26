package com.api.trip.domain.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateCommentRequest {

    @NotNull(message = "articleId를 입력해 주세요.")
    private Long articleId;

    private Long parentId;

    @NotBlank(message = "댓글을 입력해 주세요.")
    @Size(max = 50, message = "댓글은 최대 50자까지 가능합니다.")
    private String content;
}
