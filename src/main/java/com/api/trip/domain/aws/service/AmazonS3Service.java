package com.api.trip.domain.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.api.trip.common.exception.ErrorCode;
import com.api.trip.common.exception.custom_exception.AwsS3Exception;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AmazonS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Getter
    @Value("${cloud.aws.default-image}")
    private String defaultProfileImg;

    public String upload(MultipartFile profileImg) {

        // 파일 확장자 분리 (.png, .jpg, .gif)
        String ext = Optional.ofNullable(profileImg.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.split("\\.")[1])
                .orElse("");

        // 파일 이름은 UUID.ext 형식으로 업로드
        String fileName = "%s.%s".formatted(UUID.randomUUID(), ext);
        String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        String s3location = bucket + "/members/" + formatDate;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(profileImg.getSize());
        metadata.setContentType(profileImg.getContentType());

        try {
            log.debug("uploading aws s3.. upload path: {}", s3location + fileName);
            amazonS3.putObject(s3location, fileName, profileImg.getInputStream(), metadata);
        } catch (IOException e) {
            throw new AwsS3Exception(ErrorCode.AWS_FAIL_UPLOAD);
        }

        return String.valueOf(amazonS3.getUrl(s3location, fileName));
    }

    // TODO: 회원 정보 수정 구현시 필요
    public void delete(String key) {
        amazonS3.deleteObject(bucket, key);
    }

}
