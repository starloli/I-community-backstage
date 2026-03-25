package com.example.demo2.dto.response;

import com.example.demo2.entity.Facility;

public record FacilityResponse(
    Integer facilityId,
    String name,
    String description,
    Integer capacity,
    String openTime,
    String closeTime,
    boolean isAvailable
) {
    public static FacilityResponse from(Facility facility) {
        return new FacilityResponse(
            facility.getFacilityId(),
            facility.getName(),
            facility.getDescription(),
            facility.getCapacity(),
            facility.getOpenTime(),
            facility.getCloseTime(),
            facility.isAvailable()
        );
    }
}
