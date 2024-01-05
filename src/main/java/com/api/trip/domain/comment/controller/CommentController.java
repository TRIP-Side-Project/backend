package com.api.trip.domain.comment.controller;

import com.api.trip.domain.comment.controller.dto.CreateCommentRequest;
import com.api.trip.domain.comment.controller.dto.GetCommentsResponse;
import com.api.trip.domain.comment.controller.dto.GetMyCommentsResponse;
import com.api.trip.domain.comment.controller.dto.UpdateCommentRequest;
import com.api.trip.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "(대)댓글 등록")
    @PostMapping
    public ResponseEntity<Long> createComment(@RequestBody @Valid CreateCommentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.createComment(request, email));
    }

    @Operation(summary = "(대)댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long commentId, @RequestBody @Valid UpdateCommentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.updateComment(commentId, request, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "(대)댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.deleteComment(commentId, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "(대)댓글 목록 조회")
    @GetMapping
    public ResponseEntity<GetCommentsResponse> getComments(Long articleId) {
        return ResponseEntity.ok(commentService.getComments(articleId));
    }

    @Operation(summary = "내가 쓴 (대)댓글 목록 조회")
    @GetMapping("/me")
    public ResponseEntity<GetMyCommentsResponse> getMyComments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.getMyComments(email));
    }
}
