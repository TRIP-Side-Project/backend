package com.api.trip.common.exception.custom_exception;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;

public class NotMatchException extends CustomException {
    public NotMatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
