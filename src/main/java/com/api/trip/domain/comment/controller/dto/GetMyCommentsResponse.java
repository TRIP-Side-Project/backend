package com.api.trip.domain.comment.controller.dto;

import com.api.trip.domain.comment.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetMyCommentsResponse {

    private List<CommentDto> comments;

    public static GetMyCommentsResponse fromEntities(List<Comment> comments) {
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentDto::fromEntity)
                .toList();

        return new GetMyCommentsResponse(commentDtos);
    }

    @Getter
    @Builder
    private static class CommentDto {

        private Long commentId;
        private Long writerId;
        private String writerNickname;
        private Long articleId;
        private String content;
        private Long parentId;
        private LocalDateTime createdAt;

        private static CommentDto fromEntity(Comment comment) {
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
}
