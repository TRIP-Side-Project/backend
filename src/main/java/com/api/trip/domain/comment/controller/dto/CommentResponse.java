package com.api.trip.domain.comment.controller.dto;

import com.api.trip.domain.comment.model.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResponse {

    private Long commentId;
    private Long writerId;
    private String writerNickname;
    private Long articleId;
    private String content;
    private Long parentId;
    private LocalDateTime createdAt;

    @Setter
    private List<CommentResponse> comments;

    public static CommentResponse fromEntity(Comment comment) {
        return builder()
                .commentId(comment.getId())
                .writerId(comment.getWriter().getId())
                .writerNickname(comment.getWriter().getNickname())
                .articleId(comment.getArticle().getId())
                .content(comment.getContent())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
