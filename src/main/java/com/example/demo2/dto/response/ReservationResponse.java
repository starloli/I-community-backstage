package com.example.demo2.dto.response;


import com.example.demo2.entity.Facility;
import com.example.demo2.entity.Reservation;
import com.example.demo2.entity.User;
import com.example.demo2.enums.ReservationStatus;

public record ReservationResponse(
    User user,
    Facility facility,
    String date,
    String startTime,
    String endTime,
    Integer attendees,
    ReservationStatus status
) {
    public static ReservationResponse from(Reservation reservation){
        return new ReservationResponse(
            reservation.getUser(),
            reservation.getFacility(),
            reservation.getDate(),
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getAttendees(),
            reservation.getStatus()
        );
    }
}