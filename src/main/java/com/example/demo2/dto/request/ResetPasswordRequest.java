package com.example.demo2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    
    @NotNull
    @NotBlank
    String token;
    
    @NotNull
    @NotBlank
    String newPassword;
}
