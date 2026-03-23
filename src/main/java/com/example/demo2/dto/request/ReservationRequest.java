package com.example.demo2.dto.request;

import com.example.demo2.entity.Facility;
import com.example.demo2.entity.User;

public record ReservationRequest(
    User user,
    Facility facility,
    String date,
    String startTime,
    String endTime,
    Integer attendees
) {
}
