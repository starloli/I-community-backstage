package com.example.demo2.dto.response;

import java.time.LocalDateTime;

import com.example.demo2.entity.User;
import com.example.demo2.entity.Visitor;
import com.example.demo2.enums.VisitorStatus;

import lombok.Data;
@Data
public class VisitorResponse {
	private String visitorName;
private String visitorPhone;
private String licensePlate;
private String UserHostUnitNumber;
private String UserHostName;
private String purpose;
private LocalDateTime estimatedTime;
private LocalDateTime checkInTime;
private LocalDateTime checkOutTime;
//登記人員（警衛）
private String registeredBy;
private VisitorStatus status;


public static VisitorResponse fromEntity(Visitor visitor) {
    VisitorResponse resp = new VisitorResponse();
    resp.setVisitorName(visitor.getVisitorName());
    resp.setVisitorPhone(visitor.getVisitorPhone());
    resp.setLicensePlate(visitor.getLicensePlate());
    resp.setUserHostUnitNumber(visitor.getHostUser().getUnitNumber());
    resp.setUserHostName(visitor.getHostUser().getFullName());
    resp.setPurpose(visitor.getPurpose());
    resp.setEstimatedTime(visitor.getEstimatedTime());
    resp.setCheckInTime(visitor.getCheckInTime());
    resp.setCheckOutTime(visitor.getCheckOutTime());
    resp.setRegisteredBy(visitor.getRegisteredBy());
    resp.setStatus(visitor.getStatus());
    return resp;}

}







