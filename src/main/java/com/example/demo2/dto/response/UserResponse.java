package com.example.demo2.dto.response;

import java.math.BigDecimal;

import com.example.demo2.entity.User;


public record UserResponse(
    Integer userId,

    String fullName,
    String phone,
    String email,
    String unitNumber,
    BigDecimal squareFootage,
    Integer carParkingSpace,
    Integer motorParkingSpace,
    boolean isActive
) {
    public static UserResponse from(User user) {
    	return new UserResponse(
                user.getUserId(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getUnitNumber(),
                user.getSquareFootage(),
                user.getCarParkingSpace(),
                user.getMotorParkingSpace(),
                user.getIs_active()
        );
    }
}
