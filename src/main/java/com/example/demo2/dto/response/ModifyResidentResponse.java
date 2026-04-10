package com.example.demo2.dto.response;

import java.math.BigDecimal;

import com.example.demo2.entity.User;

public record ModifyResidentResponse (
		Integer userId,
	    String userName,
	    String fullName,
	    String email,
	    String unitNumber,
	    BigDecimal squareFootage,
	    Integer carParkingSpace,
	    String phone,            // 第 8 位: String
	    Integer motorParkingSpace
    
) {public static ModifyResidentResponse from(User user) {
	return new ModifyResidentResponse(
			user.getUserId(),
            user.getUserName(),
            user.getFullName(),
            user.getEmail(),
            user.getUnitNumber(),
            user.getSquareFootage(),
            user.getCarParkingSpace(),
            user.getPhone(),             // 對應第 8 個參數
            user.getMotorParkingSpace()
           
    );
}
	
}
