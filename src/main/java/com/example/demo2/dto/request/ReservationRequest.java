package com.example.demo2.dto.request;

public record ReservationRequest(
    Integer userId,
    Integer facilityId,
    String date,
    String startTime,
    String endTime,
    Integer attendees
) {
}
