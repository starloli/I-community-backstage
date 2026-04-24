package com.example.demo2.dto.response;

import com.example.demo2.entity.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarResponse {
    
    private Integer id;
    private String date;   // 2026-04-04
    private String title;

    public static CalendarResponse from(Calendar c) {
        return new CalendarResponse(c.getId(), c.getDate().toString(), c.getTitle());
    }
}
