package com.example.demo2.entity;

import java.time.LocalDateTime;

import com.example.demo2.enums.VisitorStatus;

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
@Table(name = "visitors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Visitor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer visitorId;

    @Column(nullable = false)
    private String visitorName;

    private String visitorPhone;

    private String licensePlate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    private String purpose;

    @Column(nullable = false)
    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    @Column(nullable = false)
    private Integer registeredBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitorStatus status;
}
