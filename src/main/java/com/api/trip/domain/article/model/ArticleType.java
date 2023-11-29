package com.api.trip.domain.article.model;

public enum ArticleType {
    RECOMMEND("에디터추천"),
    REVIEW("여행 후기");

    private String value;

    ArticleType(String value) {
        this.value = value;
    }
}
