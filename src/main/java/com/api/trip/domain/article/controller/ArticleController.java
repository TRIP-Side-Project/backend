package com.api.trip.domain.article.controller;

import com.api.trip.domain.article.controller.dto.CreateArticleRequest;
import com.api.trip.domain.article.controller.dto.GetArticlesResponse;
import com.api.trip.domain.article.controller.dto.GetMyArticlesResponse;
import com.api.trip.domain.article.controller.dto.ReadArticleResponse;
import com.api.trip.domain.article.controller.dto.UpdateArticleRequest;
import com.api.trip.domain.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Long> createArticle(@RequestBody CreateArticleRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(articleService.createArticle(request, email));
    }

    @PatchMapping("/{articleId}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long articleId, @RequestBody UpdateArticleRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        articleService.updateArticle(articleId, request, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        articleService.deleteArticle(articleId, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ReadArticleResponse> readArticle(@PathVariable Long articleId) {
        return ResponseEntity.ok(articleService.readArticle(articleId));
    }

    @GetMapping
    public ResponseEntity<GetArticlesResponse> getArticles(
            @PageableDefault(size = 8) Pageable pageable,
            @RequestParam(value = "filter", required = false) String filter
    ) {
        return ResponseEntity.ok(articleService.getArticles(pageable, filter));
    }

    @GetMapping("/me")
    public ResponseEntity<GetMyArticlesResponse> getMyArticles() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(articleService.getMyArticles(email));
    }
}
