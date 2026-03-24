package com.example.demo2.exception;

public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException() {
        super("Invalid email or password.");
    }
}
