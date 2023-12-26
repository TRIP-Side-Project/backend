package com.api.trip.domain.article.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateArticleRequest {

    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(min = 5, max = 30, message = "제목은 최소 5자에서 최대 30자까지 가능합니다.")
    private String title;

    @Size(max = 3, message = "태그는 최대 3개까지 등록할 수 있습니다.")
    private List<String> tags;

    @NotBlank(message = "본문을 입력해 주세요.")
    @Size(max = 15000, message = "본문의 크기가 너무 큽니다.")
    private String content;
}
