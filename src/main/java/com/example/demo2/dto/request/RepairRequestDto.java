package com.example.demo2.dto.request;

public record RepairRequestDto(
    String location,
    String category,
    String description,
    String imageUrl,
    String submittedAt
) {}
