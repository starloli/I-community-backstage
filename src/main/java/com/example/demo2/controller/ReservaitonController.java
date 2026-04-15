package com.example.demo2.controller;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.ReservationRequest;
import com.example.demo2.dto.response.ReservationResponse;
import com.example.demo2.service.NotificationService;
import com.example.demo2.service.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaitonController {

    private final ReservationService reservationService;
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> reserveFacility(
            @RequestBody ReservationRequest reservation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.reserveFacility(reservation));
    }

    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByUserId(
            @PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }

    @GetMapping("/byFacilityId/{facilityId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByFacilityId(
            @PathVariable("facilityId") Integer facilityId) {
        return ResponseEntity.ok(reservationService.getReservationsByFacilityId(facilityId));
    }

    @GetMapping("/byFacilityIdAndUserId/{facilityId}/{userId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByFacilityId(
            @PathVariable("facilityId") Integer facilityId, @PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(reservationService.getReservationsByFacilityIdAndUserId(facilityId, userId));
    }

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelReservation(
            @RequestBody Integer reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("message", "已取消預約"));
    }

    @PutMapping("/remindTime")
    public ResponseEntity<?> remindTime(@RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timecron) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", notificationService.updateSchedule(timecron)));
    }
}
