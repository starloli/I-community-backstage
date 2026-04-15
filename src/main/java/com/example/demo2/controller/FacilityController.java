package com.example.demo2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.service.FacilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facility")
@CrossOrigin(origins = "http://localhost:4200")
public class FacilityController {

    private final FacilityService facilityService;
    
    @GetMapping
    public ResponseEntity<List<FacilityResponse>> getFacilities() {
        return ResponseEntity.ok(facilityService.getFacilities());
    }
}
