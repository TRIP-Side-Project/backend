package com.api.trip.domain.comment.controller.dto;

import com.api.trip.domain.comment.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class GetCommentsResponse {

    private List<CommentDto> comments;

    public static GetCommentsResponse of(List<Comment> comments) {
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentDto::of)
                .toList();

        Map<Long, List<CommentDto>> groupByParentId = commentDtos.stream()
                .collect(Collectors.groupingBy(commentDto -> commentDto.getParentId() != null ? commentDto.getParentId() : 0));

        commentDtos = commentDtos.stream()
                .filter(commentDto -> {
                    if (commentDto.getParentId() == null) {
                        commentDto.setChildren(groupByParentId.get(commentDto.getCommentId()));
                        return true;
                    }
                    return false;
                })
                .toList();

        return new GetCommentsResponse(commentDtos);
    }

    @Getter
    @Builder
    private static class CommentDto {

        private Long commentId;
        private Long writerId;
        private String writerNickname;
        private String writerProfileImg;
        private Long articleId;
        private String content;
        private Long parentId;
        private LocalDateTime createdAt;

        @Setter
        private List<CommentDto> children;

        private static CommentDto of(Comment comment) {
            return builder()
                    .commentId(comment.getId())
                    .writerId(comment.getWriter().getId())
                    .writerNickname(comment.getWriter().getNickname())
                    .writerProfileImg(comment.getWriter().getProfileImg())
                    .articleId(comment.getArticle().getId())
                    .content(comment.getContent())
                    .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                    .createdAt(comment.getCreatedAt())
                    .build();
        }
    }
}
