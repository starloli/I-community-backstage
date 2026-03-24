package com.example.demo2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.FacilityRegistRequest;
import com.example.demo2.dto.response.FacilityResponse;
=======
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

>>>>>>> parent of 5ce4729 (1)
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
        Authentication authentication
    ) {
        String name = authentication.getName();
        return ResponseEntity.ok(userService.getProfileByName(name));
    }
<<<<<<< HEAD

    @PostMapping("/regist-facility")
    public ResponseEntity<FacilityResponse> registFacility(
            @RequestBody FacilityRegistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.registFacility(request));
    }

    @GetMapping("/facilities")
    public ResponseEntity<ArrayList<FacilityResponse>> getFacilities() {
        return ResponseEntity.ok(facilityService.getFacilities());
    }
=======
>>>>>>> parent of 5ce4729 (1)
}
