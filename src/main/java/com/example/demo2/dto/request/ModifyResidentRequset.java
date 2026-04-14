package com.example.demo2.dto.request;

import java.math.BigDecimal;

import com.example.demo2.enums.UserRole;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModifyResidentRequset {
	@NotNull(message = "使用者ID不可為空")
	private Integer userId;
	@NotBlank(message = "使用者名稱不可為空")
	private String fullName;
	@Email(message = "信箱格式不正確")
	private String email;
	private String phone;
	@NotBlank(message = "房號不可為空")
	private String unitNumber;
	private UserRole role;
	private Boolean is_active;
	@NotNull(message = "坪數不可為空")
	@DecimalMin(value = "0.0", message = "坪數不能為負")
	private BigDecimal squareFootage;
	@NotNull(message = "汽車位數量不可為空")
	@Min(value = 0, message = "汽車位數量不能為負")
	private Integer carParkingSpace;
	@NotNull(message = "機車位數量不可為空")
	@Min(value = 0, message = "機車位數量不能為負")
	private Integer motorParkingSpace;
}
