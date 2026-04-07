package com.example.demo2.controller;

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

import com.example.demo2.dto.request.FacilityRequest;
import com.example.demo2.dto.request.ReservationRequest;
import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.dto.response.PackageResponse;
import com.example.demo2.dto.response.RepairResponse;
import com.example.demo2.dto.response.ReservationResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.service.FacilityService;
import com.example.demo2.service.PackageService;
import com.example.demo2.service.RepairRequestService;
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
    private final RepairRequestService repairRequestService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            Authentication authentication) {
        String name = authentication.getName();
        return ResponseEntity.ok(userService.getProfileByName(name));
    }

    @GetMapping("/facilities")
    public ResponseEntity<List<FacilityResponse>> getFacilities() {
        return ResponseEntity.ok(facilityService.getFacilities());
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponse> reserveFacility(
            @RequestBody ReservationRequest reservation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.reserveFacility(reservation));
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

    @GetMapping("/package")
    public ResponseEntity<List<PackageResponse>> getMyPackages(
            Authentication authentication) {
        String name = authentication.getName();
        return ResponseEntity.ok(packageService.searchByUser(name));
    }

    @GetMapping("/repair")
    public ResponseEntity<List<RepairResponse>> getMyRepair(
            Authentication authentication) {
        String name = authentication.getName();
        return ResponseEntity.ok(repairRequestService.searchUserAll(name));
    }
}
