package com.api.trip.domain.articlefile.service;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;
import com.api.trip.domain.articlefile.model.ArticleFile;
import com.api.trip.domain.articlefile.repository.ArticleFileRepository;
import com.api.trip.domain.articlefile.uploader.ArticleFileUploader;
import com.api.trip.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleFileService {

    private final MemberRepository memberRepository;
    private final ArticleFileRepository articleFileRepository;
    private final ArticleFileUploader articleFileUploader;

    public String upload(MultipartFile multipartFile, String email) {
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        String url = null;
        try {
            url = articleFileUploader.upload(multipartFile, UUID.randomUUID().toString());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.UPLOAD_FAILED);
        }

        return articleFileRepository.save(
                        ArticleFile.builder()
                                .url(url)
                                .build()
                )
                .getUrl();
    }

    public void deleteTemporaryArticleFilesBefore(LocalDateTime localDateTime) {
        List<ArticleFile> articleFiles = articleFileRepository.findAllByArticleNullAndCreatedAtBefore(localDateTime);

        articleFileRepository.deleteAllInBatch(articleFiles);

        articleFiles.forEach(articleFile -> articleFileUploader.delete(articleFile.getUrl()));
    }
}
