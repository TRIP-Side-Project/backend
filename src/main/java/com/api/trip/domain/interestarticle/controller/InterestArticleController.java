package com.api.trip.domain.interestarticle.controller;

import com.api.trip.domain.interestarticle.controller.dto.CreateInterestArticleRequest;
import com.api.trip.domain.interestarticle.controller.dto.GetMyInterestArticlesResponse;
import com.api.trip.domain.interestarticle.service.InterestArticleService;
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

    @PostMapping
    public ResponseEntity<Long> createInterestArticle(@RequestBody @Valid CreateInterestArticleRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(interestArticleService.createInterestArticle(request, email));
    }

    @DeleteMapping("/{interestArticleId}")
    public ResponseEntity<Void> deleteInterestArticle(@PathVariable Long interestArticleId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        interestArticleService.deleteInterestArticle(interestArticleId, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<GetMyInterestArticlesResponse> getMyInterestArticles() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(interestArticleService.getMyInterestArticles(email));
    }
}
