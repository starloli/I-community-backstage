package com.example.demo2.dto.response;

import java.time.LocalTime;

import com.example.demo2.entity.Facility;

public record FacilityResponse(
    String name,
    String deString,
    Integer capacity,
    LocalTime openTime,
    LocalTime closeTime,
    boolean isAvailable
) {
    public static FacilityResponse from(Facility facility) {
        return new FacilityResponse(
            facility.getName(),
            facility.getDescription(),
            facility.getCapacity(),
            facility.getOpenTime(),
            facility.getCloseTime(),
            facility.isAvailable()
        );
    }
}
