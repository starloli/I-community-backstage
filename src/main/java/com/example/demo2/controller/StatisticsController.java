package com.example.demo2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
@CrossOrigin(origins = "http://localhost:4200")
public class StatisticsController {
    
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<Long> getUserNum() {
        return ResponseEntity.ok(userService.getTotalNumber());
    }   
}
