package com.example.demo2.dto.request;

import com.example.demo2.entity.Facility;
import com.example.demo2.entity.User;

public record SearchReservationRequest(
    User user,
    Facility facility
) {
}
