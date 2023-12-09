package com.api.trip.domain.comment.controller;

import com.api.trip.domain.comment.controller.dto.CommentResponse;
import com.api.trip.domain.comment.controller.dto.CreateCommentRequest;
import com.api.trip.domain.comment.controller.dto.UpdateCommentRequest;
import com.api.trip.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Long> createComment(@RequestBody CreateCommentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.createComment(request, email));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.updateComment(commentId, request, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.deleteComment(commentId, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(Long articleId) {
        return ResponseEntity.ok(commentService.getComments(articleId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<CommentResponse>> getMyComments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.getMyComments(email));
    }
}
