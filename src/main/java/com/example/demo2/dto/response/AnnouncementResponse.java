package com.example.demo2.dto.response;

import java.time.LocalDateTime;

import com.example.demo2.entity.Announcement;

public record AnnouncementResponse(
    Integer announcementId,
    String title,
    String content,
    String category,
    String authorName,
    Boolean isPinned,
    LocalDateTime publishedAt,
    LocalDateTime expiresAt
) {
    public static AnnouncementResponse from(Announcement announcement) {
        return new AnnouncementResponse(
            announcement.getAnnouncementId(),
            announcement.getTitle(),
            announcement.getContent(),
            announcement.getCategory(),
            announcement.getAuthor().getFullName(),
            announcement.getIsPinned(),
            announcement.getPublishedAt(),
            announcement.getExpiresAt()
        );
    }
}
