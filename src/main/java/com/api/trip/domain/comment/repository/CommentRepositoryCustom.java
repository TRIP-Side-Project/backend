package com.api.trip.domain.comment.repository;

import com.api.trip.domain.article.model.Article;
import com.api.trip.domain.comment.model.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findComments(Article article);
}
