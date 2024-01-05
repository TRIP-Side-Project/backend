package com.api.trip.domain.article.controller;

import com.api.trip.domain.article.controller.dto.*;
import com.api.trip.domain.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "게시글 등록")
    @PostMapping
    public ResponseEntity<Long> createArticle(@RequestBody @Valid CreateArticleRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(articleService.createArticle(request, email));
    }

    @Operation(summary = "게시글 수정")
    @PatchMapping("/{articleId}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long articleId, @RequestBody @Valid UpdateArticleRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        articleService.updateArticle(articleId, request, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        articleService.deleteArticle(articleId, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{articleId}")
    public ResponseEntity<ReadArticleResponse> readArticle(@PathVariable Long articleId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(articleService.readArticle(articleId, email));
    }

    @Operation(summary = "게시글 목록 조회 (게시글 검색)")
    @GetMapping
    public ResponseEntity<GetArticlesResponse> getArticles(
            @PageableDefault(size = 8) Pageable pageable,
            @RequestParam(value = "sortCode", defaultValue = "0") int sortCode,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tag", required = false) String tagName
    ) {
        return ResponseEntity.ok(articleService.getArticles(pageable, sortCode, category, title, tagName));
    }

    @Operation(summary = "내가 쓴 게시글 목록 조회")
    @GetMapping("/me")
    public ResponseEntity<GetMyArticlesResponse> getMyArticles() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(articleService.getMyArticles(email));
    }
}
