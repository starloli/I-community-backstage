package com.example.demo2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.LoginRequest;
import com.example.demo2.dto.request.UserCreateRequest;
import com.example.demo2.dto.response.LoginResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.service.AuthService;
import com.example.demo2.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    
        private final AuthService authService;
        private final UserService userService;

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                @RequestBody LoginRequest request
        ) {
                return ResponseEntity.ok(authService.login(request));
        }

        @PostMapping("/register")
        public ResponseEntity<UserResponse> registerUser(
                @RequestBody UserCreateRequest request
        ) {
                return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
        }
}
