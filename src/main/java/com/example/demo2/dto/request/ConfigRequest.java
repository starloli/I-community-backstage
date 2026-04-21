package com.example.demo2.dto.request;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ConfigRequest(
    @JsonFormat(pattern = "HH:mm")
    LocalTime timecron
) {
}
