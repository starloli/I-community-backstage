package com.example.demo2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.RepairRequestDto;
import com.example.demo2.dto.response.RepairResponse;
import com.example.demo2.service.RepairRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repair")
@CrossOrigin(origins = "http://localhost:4200")
public class RepairRequestController {
    
    private final RepairRequestService service;

    @GetMapping
    public ResponseEntity<List<RepairResponse>> getAll() {
        return ResponseEntity.ok(service.searchAll());
    }

    @PostMapping
    public ResponseEntity<RepairResponse> postRepairRequest(
        Authentication authentication,
        @Valid @RequestBody RepairRequestDto d
    ) {
        String name = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRepairRequest(name, d));
    }
}
