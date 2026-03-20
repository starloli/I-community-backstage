package com.example.demo2.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "announcements")
@Data
@NoArgsConstructor
public class Announcement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer announcementId;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String content;
    
    private String category;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @Column(nullable = false)
    private Boolean isPinned;
    
    @Column(nullable = false)
    private LocalDateTime publishedAt;
    
    private LocalDateTime expiresAt;

    public Announcement(
        String title,
        String content,
        String category,
        User author,
        Boolean isPinned,
        LocalDateTime expiresAt
    ) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.isPinned = isPinned;
        this.publishedAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
    }
}
