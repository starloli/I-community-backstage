package com.example.demo2.dto.response;

import com.example.demo2.entity.User;

public record UserResponse(
    Integer userId,
    String userName,
    String fullName,
    String email,
    String phone,
    String unitNumber,
    Boolean isActive
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getUserName(),
            user.getFullName(),
            user.getEmail(),
            user.getPhone(),
            user.getUnitNumber(),
            user.getIs_active()
        );
    }
}
