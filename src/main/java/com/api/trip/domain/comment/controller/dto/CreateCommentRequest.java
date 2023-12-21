package com.api.trip.domain.comment.controller.dto;

import lombok.Getter;

@Getter
public class CreateCommentRequest {

    private Long articleId;
    private Long parentId;
    private String content;
}
