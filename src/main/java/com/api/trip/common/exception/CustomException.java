package com.api.trip.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    protected CustomException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
    private ErrorCode errorCode;


}
