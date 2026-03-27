package com.example.demo2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest (
    
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20, message = "Username must be 4-20 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9_]+$",
        message = "Username can only contain letters, numbers and underscore"
    )
    String userName,

    String password,

    String fullName,

    @NotNull
    @NotBlank
    @Email
    String email,

    String phone,

    String unitNumber
) {}
