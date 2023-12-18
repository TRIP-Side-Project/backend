package com.api.trip.domain.articlefile.uploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class ArticleFileUploader {

    private static final String FOLDER_NAME = "/articles";
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String fileName) {
        InputStream inputStream;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        amazonS3.putObject(bucket + FOLDER_NAME, fileName, inputStream, objectMetadata);

        return amazonS3.getUrl(bucket + FOLDER_NAME, fileName).toString();
    }

    public void delete(String url) {
        String fileName = url.split(FOLDER_NAME + "/")[1];
        amazonS3.deleteObject(bucket + FOLDER_NAME, fileName);
    }
}
