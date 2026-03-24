package com.example.demo2.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.FacilityRegistRequest;
import com.example.demo2.dto.request.ReservationRequest;
import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.dto.response.ReservationResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.service.FacilityService;
import com.example.demo2.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final FacilityService facilityService;


    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            Authentication authentication) {
        String name = authentication.getName();
        return ResponseEntity.ok(userService.getProfileByName(name));
    }

    @PostMapping("/regist-facility")
    public ResponseEntity<FacilityResponse> registFacility(
            @RequestBody FacilityRegistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.registFacility(request));
    }

    @GetMapping("/facilities")
    public ResponseEntity<ArrayList<FacilityResponse>> getFacilities() {
        return ResponseEntity.ok(facilityService.getFacilities());
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponse> reserveFacility(@RequestBody ReservationRequest reservation) {
        return ResponseEntity.ok(facilityService.reserveFacility(reservation));
    }

}
