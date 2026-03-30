package com.example.demo2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.response.StatisticsResponse;
import com.example.demo2.service.AnnouncementService;
import com.example.demo2.service.PackageService;
import com.example.demo2.service.RepairRequestService;
import com.example.demo2.service.UserService;
import com.example.demo2.service.VisitorService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
@CrossOrigin(origins = "http://localhost:4200")
public class StatisticsController {
    
    private final UserService userService;
    private final VisitorService visitorService;
    private final PackageService packageService;
    private final AnnouncementService announcementService;
    private final RepairRequestService repairRequestService;

    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatistics() {
        StatisticsResponse response = new StatisticsResponse(
                userService.getTotalNumber(),
                visitorService.findTodayVisitorNum(),
                packageService.geWaittingNumber(),
                repairRequestService.searchPendingNum(),
                announcementService.findRecentThree(),
                visitorService.findRecentThree()
            );
        return ResponseEntity.ok(response);
    }
}
