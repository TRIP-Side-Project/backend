package com.api.trip.domain.interestarticle.controller;

import com.api.trip.domain.interestarticle.controller.dto.CreateInterestArticleRequest;
import com.api.trip.domain.interestarticle.controller.dto.GetMyInterestArticlesResponse;
import com.api.trip.domain.interestarticle.service.InterestArticleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interest-articles")
@RequiredArgsConstructor
public class InterestArticleController {

    private final InterestArticleService interestArticleService;

    @Operation(summary = "게시글 좋아요")
    @PostMapping
    public ResponseEntity<Long> createInterestArticle(@RequestBody @Valid CreateInterestArticleRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(interestArticleService.createInterestArticle(request, email));
    }

    @Operation(summary = "게시글 좋아요 취소")
    @DeleteMapping("/{interestArticleId}")
    public ResponseEntity<Void> deleteInterestArticle(@PathVariable Long interestArticleId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        interestArticleService.deleteInterestArticle(interestArticleId, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내가 좋아한 게시글 목록 조회")
    @GetMapping("/me")
    public ResponseEntity<GetMyInterestArticlesResponse> getMyInterestArticles() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(interestArticleService.getMyInterestArticles(email));
    }
}
