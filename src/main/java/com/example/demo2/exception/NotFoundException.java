package com.example.demo2.exception;

public class NotFoundException extends RuntimeException {
    
    public NotFoundException(String msg) {
        super(msg);
    }
}
