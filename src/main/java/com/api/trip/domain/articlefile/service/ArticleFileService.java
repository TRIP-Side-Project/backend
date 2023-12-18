package com.api.trip.domain.articlefile.service;

import com.api.trip.domain.articlefile.model.ArticleFile;
import com.api.trip.domain.articlefile.repository.ArticleFileRepository;
import com.api.trip.domain.articlefile.uploader.ArticleFileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleFileService {

    private final ArticleFileRepository articleFileRepository;
    private final ArticleFileUploader articleFileUploader;

    public String upload(MultipartFile multipartFile, String email) {
        if (Objects.equals(email, "anonymousUser")) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        String url = articleFileUploader.upload(multipartFile, UUID.randomUUID().toString());

        ArticleFile articleFile = ArticleFile.builder()
                .url(url)
                .build();

        return articleFileRepository.save(articleFile).getUrl();
    }
}
