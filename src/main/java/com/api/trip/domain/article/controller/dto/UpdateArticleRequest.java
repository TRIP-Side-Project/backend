package com.api.trip.domain.article.controller.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateArticleRequest {

    private String title;
    private List<String> tags;
    private String content;
}
