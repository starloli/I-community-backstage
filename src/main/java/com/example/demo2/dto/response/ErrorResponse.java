package com.example.demo2.dto.response;

public record ErrorResponse(

    int status,

    String error,
    
    String message
) {}
