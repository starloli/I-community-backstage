package com.example.demo2.dto.response;

import com.example.demo2.enums.PakageStatus;
import com.example.demo2.entity.Package;

public record PackageResponse(
    Integer id,
    String user,
    String unitNumber,
    String trackingNumber,
    String courier,
    String arrivedAt,
    String pickupAt,
    PakageStatus status,
    String notes,
    Boolean isNotified
) {
    public static PackageResponse from(Package p) {
        return new PackageResponse(
            p.getPackageId(),
            p.getRecipientName(),
            p.getUnitNumber(),
            p.getTrackingNumber(),
            p.getCourier(),
            p.getArrivedAt(),
            p.getPickupAt(),
            p.getStatus(),
            p.getNotes(),
            p.getNotified()
        );
    }
}
