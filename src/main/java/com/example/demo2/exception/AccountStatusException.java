package com.example.demo2.exception;

import com.example.demo2.enums.ErrorCode;

public class AccountStatusException extends RuntimeException {
    
    private final ErrorCode errorCode;

    public AccountStatusException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}   