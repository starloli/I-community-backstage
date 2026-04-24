package com.example.demo2.dto.request;

import lombok.Data;

@Data
public class SalaryGenerateRequest {

	private  Long departmentId;
	private int year;
	private int month;
}
