package com.example.demo2.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.FacilityRegistRequest;
import com.example.demo2.dto.request.LoginRequest;
import com.example.demo2.dto.request.UserCreateRequest;
import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.dto.response.LoginResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.service.AuthService;
import com.example.demo2.service.FacilityService;
import com.example.demo2.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

        private final AuthService authService;
        private final UserService userService;
        private final FacilityService facilityService;


        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                @Valid @RequestBody LoginRequest request
        ) {
                return ResponseEntity.ok(authService.login(request));
        }

        @PostMapping("/register")
        public ResponseEntity<UserResponse> registerUser(
                @Valid @RequestBody UserCreateRequest request
        ) {
                return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
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

}
