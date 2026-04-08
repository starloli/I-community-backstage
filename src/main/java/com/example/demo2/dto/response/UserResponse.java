package com.example.demo2.dto.response;

import java.math.BigDecimal;

import com.example.demo2.entity.User;


public record UserResponse(
    Integer userId,
    String userName,
    String fullName,
    String email,
    String unitNumber,
    BigDecimal squareFootage,
    Integer carParkingSpace,

    Integer motorParkingSpace

) {
    public static UserResponse from(User user) {
    	return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getFullName(),
                user.getEmail(),
                user.getUnitNumber(),
                // 💡 映射新欄位
                user.getSquareFootage(),
                user.getCarParkingSpace(),
                user.getMotorParkingSpace()
           
        );
    }
}
