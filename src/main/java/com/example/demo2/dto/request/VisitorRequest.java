package com.example.demo2.dto.request;

import java.time.LocalDateTime;

import com.example.demo2.entity.User;
import com.example.demo2.enums.VisitorStatus;

import lombok.Data;
@Data
public class VisitorRequest{ 
		 private String visitorName;
		    private String visitorPhone;
		    private String licensePlate;
		    private Long hostUserId;
		    private String purpose;
		    private LocalDateTime estimatedTime;
		    private LocalDateTime checkInTime;
		    private LocalDateTime checkOutTime;
		    private String registeredBy;
		    private VisitorStatus status;}

