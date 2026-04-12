package com.example.demo2.dto.request;

import jakarta.validation.constraints.Email;

public record EmailVerifyRequest(
    @Email
    String email,
    String code
) {}
