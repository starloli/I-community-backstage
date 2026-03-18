package com.example.demo2.dto.request;

import java.time.LocalDateTime;

public record AnnouncementCreateRequest(
    String title,
    String content,
    String category,
    Boolean isPinned,
    LocalDateTime expiresAt
) {}
