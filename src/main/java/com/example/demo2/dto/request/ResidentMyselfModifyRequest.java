package com.example.demo2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResidentMyselfModifyRequest {
	@Email(message = "信箱格式不正確")
	  private String email;
@NotNull(message="手機號不能爲空")
	private String phone;

}
