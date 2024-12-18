package com.roomfinder.marketing.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // Pass error message to the superclass
        this.errorCode = errorCode;
    }
}
