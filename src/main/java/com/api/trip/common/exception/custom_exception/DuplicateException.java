package com.api.trip.common.exception.custom_exception;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;

public class DuplicateException extends CustomException {
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
