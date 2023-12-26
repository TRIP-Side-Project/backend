package com.api.trip.domain.interestarticle.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateInterestArticleRequest {

    @NotNull(message = "articleId를 입력해 주세요.")
    private Long articleId;
}
