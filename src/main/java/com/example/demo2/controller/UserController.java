package com.example.demo2.controller;

import com.example.demo2.service.PackageService;
import java.util.List;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.FacilityRegistRequest;
import com.example.demo2.dto.request.ReservationRequest;
import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.dto.response.PackageResponse;
import com.example.demo2.dto.response.ReservationResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.service.FacilityService;
import com.example.demo2.service.ReservationService;
import com.example.demo2.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final PackageService packageService;
    private final UserService userService;
    private final FacilityService facilityService;
    private final ReservationService reservationService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            Authentication authentication) {
        String name = authentication.getName();
        return ResponseEntity.ok(userService.getProfileByName(name));
    }

    @GetMapping("/package")
    public ResponseEntity<List<PackageResponse>> getMyPackages(
        Authentication authentication
    ) {
        String name = authentication.getName();
        return ResponseEntity.ok(packageService.searchByUser(name));
    }

    @PostMapping("/regist-facility")
    public ResponseEntity<FacilityResponse> registFacility(
            @RequestBody FacilityRegistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.registFacility(request));
    }

    @GetMapping("/facilities")
    public ResponseEntity<List<FacilityResponse>> getFacilities() {
        return ResponseEntity.ok(facilityService.getFacilities());
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponse> reserveFacility(
            @RequestBody ReservationRequest reservation) {
        return ResponseEntity.ok(reservationService.reserveFacility(reservation));
    }

    @GetMapping("/reservationsByUserId/{userId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByUserId(
            @PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }

    @GetMapping("/reservationsByFacilityId/{facilityId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByFacilityId(
            @PathVariable("facilityId") Integer facilityId) {
        return ResponseEntity.ok(reservationService.getReservationsByFacilityId(facilityId));
    }

    @GetMapping("/reservationsByFacilityIdAndUserId/{facilityId}/{userId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByFacilityId(
            @PathVariable("facilityId") Integer facilityId, @PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(reservationService.getReservationsByFacilityIdAndUserId(facilityId, userId));
    }

    @PutMapping("/cancelReservation")
    public ResponseEntity<?> cancelReservation(
            @RequestBody Integer reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("message", "已取消預約"));
    }
}
