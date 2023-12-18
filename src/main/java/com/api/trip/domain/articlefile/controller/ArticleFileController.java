package com.api.trip.domain.articlefile.controller;

import com.api.trip.domain.articlefile.service.ArticleFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/article-files")
@RequiredArgsConstructor
public class ArticleFileController {

    private final ArticleFileService articleFileService;

    @PostMapping
    public ResponseEntity<String> upload(MultipartFile file) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(articleFileService.upload(file, email));
    }
}
