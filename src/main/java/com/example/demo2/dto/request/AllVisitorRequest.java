package com.example.demo2.dto.request;

import java.time.LocalDateTime;

import com.example.demo2.enums.VisitorStatus;

import lombok.Data;
@Data
public class AllVisitorRequest {
private int visitorId;
	private String visitorName;
	private String visitorPhone;
    private LocalDateTime estimatedTime;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String licensePlate;
    private String purpose;

    //警衛 名字
    private String registeredBy;
    private VisitorStatus status;
    //住戶信息
    private String residentName;
    private String residentialAddress;
}
