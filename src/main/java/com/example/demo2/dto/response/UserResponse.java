package com.example.demo2.dto.response;

import java.math.BigDecimal;

import com.example.demo2.entity.User;
import com.example.demo2.enums.UserRole;
import com.example.demo2.enums.UserStatus;


public record UserResponse(
    Integer userId,
    String userName,
    UserRole role,
    String fullName,
    String phone,
    String email,
    String unitNumber,
    BigDecimal squareFootage,
    Integer carParkingSpace,
    Integer motorParkingSpace,
    UserStatus status
) {
    public static UserResponse from(User user) {
    	return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getRole(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getUnitNumber(),
                user.getSquareFootage(),
                user.getCarParkingSpace(),
                user.getMotorParkingSpace(),
                user.getStatus()
        );
    }
}
