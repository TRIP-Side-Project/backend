package com.api.trip.common.exception.custom_exception;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;

public class ForbiddenException extends CustomException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
