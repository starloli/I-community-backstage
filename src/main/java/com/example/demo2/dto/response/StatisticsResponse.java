package com.example.demo2.dto.response;

import java.util.List;

import com.example.demo2.dto.request.AllVisitorRequest;

public record StatisticsResponse(
    long userNum,
    long todayVistorNum,
    long packageWaitingNum,
    long repairPendingNum,
    List<AnnouncementResponse> recentAnnouncements,
    List<AllVisitorRequest> recentVistors
) {}
