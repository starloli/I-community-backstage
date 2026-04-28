package com.example.demo2.dto.request;

public record SuperAdminSelfRequest(
    String fullName,
    String email,
    String phone,
    String password,
    String verifyCode
) {
}
