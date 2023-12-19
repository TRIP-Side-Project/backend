package com.api.trip.common.exception.custom_exception;

import com.api.trip.common.exception.CustomException;
import com.api.trip.common.exception.ErrorCode;

public class InvalidException extends CustomException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
