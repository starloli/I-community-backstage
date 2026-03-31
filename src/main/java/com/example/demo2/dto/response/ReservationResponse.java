package com.example.demo2.dto.response;

import com.example.demo2.entity.Reservation;
import com.example.demo2.enums.ReservationStatus;

public record ReservationResponse(
    Integer reservationId,
    String userName,
    String facilityName,
    String date,
    String startTime,
    String endTime,
    Integer attendees,
    ReservationStatus status
) {
    public static ReservationResponse from(Reservation reservation){
        return new ReservationResponse(
            reservation.getReservationId(),
            reservation.getUser().getUserName(),
            reservation.getFacility().getName(),
            reservation.getDate(),
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getAttendees(),
            reservation.getStatus()
        );
    }
}