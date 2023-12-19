package com.api.trip.common.exception.custom_exception;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;

public class BadRequestException extends CustomException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}