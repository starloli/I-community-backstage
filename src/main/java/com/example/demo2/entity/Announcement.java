package com.example.demo2.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "announcements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer announcementId;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String content;
    
    private String category;
    
    @Column(nullable = false)
    private Integer authorId;
    
    @Column(nullable = false)
    private Integer is_pinned;
    
    @Column(nullable = false)
    private LocalDateTime publishedAt;
    
    private LocalDateTime expiresAt;
}
