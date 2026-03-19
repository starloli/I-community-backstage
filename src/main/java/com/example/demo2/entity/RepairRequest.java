package com.example.demo2.entity;

import java.time.LocalDateTime;

import com.example.demo2.enums.RepairStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "repair_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer repairId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String location;

    private String category;

    private String description;

    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RepairStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "handler_id", nullable = false)
    private User handler;

    private LocalDateTime submittedAt;

    private LocalDateTime resolvedAt;
}
