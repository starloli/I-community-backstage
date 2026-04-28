package com.example.demo2.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CalendarRequest {
    
    private LocalDate date;
    private String title;
}
