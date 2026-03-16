package com.example.demo2.dto.request;

import java.time.LocalTime;

public record FacilityRegistRequest(
        String name,

        String description,

        Integer capacity,

        LocalTime openTime,

        LocalTime closeTime,

        boolean isAvailable
    ) {
}