package com.example.demo2.dto.request;

public record FacilityRegistRequest(
        String name,

        String description,

        Integer capacity,

        String openTime,

        String closeTime,

        Boolean isReservable,

        Boolean isAvailable) {
}