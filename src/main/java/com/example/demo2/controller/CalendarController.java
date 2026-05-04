package com.example.demo2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.CalendarRequest;
import com.example.demo2.dto.response.CalendarResponse;
import com.example.demo2.service.CalendarService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
@CrossOrigin(origins = "http://localhost:4200")
public class CalendarController {
    
    private final CalendarService service;

    @GetMapping("/holiday")
    public List<CalendarResponse> getHolidays(
        @RequestParam("start") String start
    ) {
        return service.getHolidays(start);
    }

    @GetMapping
    public List<CalendarResponse> getEvents(
        @RequestParam("start") String start
    ) {
        return service.getEvents(start);
    }

    @GetMapping("/reservation")
    public List<CalendarResponse> getReservations(
        @RequestParam("start") String start,
        Authentication authentication
    ) {
        String name = authentication.getName();
        return service.getReservations(start, name);
    }

    @GetMapping("/bill")
    public List<CalendarResponse> getBills(
        @RequestParam("start") String start,
        Authentication authentication
    ) {
        String name = authentication.getName();
        return service.getBills(start, name);
    }

    @PostMapping
    public CalendarResponse postEvent(
        @RequestBody CalendarRequest r
    ) {   
        return service.createEvent(r);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
        @PathVariable("id") Integer id
    ) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
