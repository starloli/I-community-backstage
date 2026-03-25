package com.example.demo2.dto.response;

import lombok.Data;

@Data
public class VisitorGetUserMassageResponse {
	private Integer userId; 
    private String fullName;
    private String phone;
    private String unitNumber;
}
