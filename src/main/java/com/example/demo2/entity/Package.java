package com.example.demo2.entity;

import java.time.LocalDateTime;

import com.example.demo2.enums.PackageStatus;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
public class Package {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer packageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String tracking_number;

    private String courier;

    @Column(nullable = false)
    private LocalDateTime arrived_at;

    private LocalDateTime pickup_at;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PackageStatus status;

    private String notes;
    
    @Column(nullable = false)
    private boolean isNotified;
}
