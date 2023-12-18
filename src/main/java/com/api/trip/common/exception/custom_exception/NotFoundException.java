package com.api.trip.common.exception.custom_exception;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
