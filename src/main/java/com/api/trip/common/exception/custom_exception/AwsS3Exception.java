package com.api.trip.common.exception.custom_exception;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;

public class AwsS3Exception extends CustomException {
    public AwsS3Exception(ErrorCode errorCode) {
        super(errorCode);
    }
}