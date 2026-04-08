package com.example.demo2.dto.request;

import com.example.demo2.enums.RepairStatus;

public record RepairUpdateRequest (
    String location,
    String category,
    String description,
    String imageUrl,
    RepairStatus status
) {}
