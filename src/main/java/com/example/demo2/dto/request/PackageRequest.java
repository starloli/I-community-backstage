package com.example.demo2.dto.request;

public record PackageRequest(
    String recipientName,
    String phoneNumber,
    String unitNumber,
    String trackingNumber,
    String courier,
    String arrivedAt,
    String notes
) {}
