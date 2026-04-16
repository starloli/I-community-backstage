package com.example.demo2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.FacilityRequest;
import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.service.FacilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facility")
@CrossOrigin(origins = "http://localhost:4200")
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping("/regist-facility")
    public ResponseEntity<FacilityResponse> registFacility(
            @RequestBody FacilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.registFacility(request));
    }

    @PutMapping("/update-facility/{facilityId}")
    public ResponseEntity<FacilityResponse> updateFacility(
            @PathVariable("facilityId") Integer facilityId, @RequestBody FacilityRequest facilityRequest) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(facilityService.updateFacility(facilityId, facilityRequest));
    }

    @DeleteMapping("/delete-facility/{facilityId}")
    public ResponseEntity<Void> deleteFacility(@PathVariable("facilityId") Integer facilityId) {
        facilityService.deleteFacility(facilityId);
        return ResponseEntity.noContent().build();
    }

}
