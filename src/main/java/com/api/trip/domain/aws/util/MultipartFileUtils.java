package com.api.trip.domain.aws.util;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MultipartFileUtils {

    final static String[] PERMISSION_PROFILE_FILE_MIME_TYPE = {"image/jpeg", "image/png", "image/gif"};

    // 파일 변조 여부 검사
    public static boolean isPermission(InputStream inputStream) throws IOException {
        String mimeType = new Tika().detect(inputStream);
        return Arrays.stream(PERMISSION_PROFILE_FILE_MIME_TYPE).anyMatch(type -> type.equalsIgnoreCase(mimeType));
    }
}
