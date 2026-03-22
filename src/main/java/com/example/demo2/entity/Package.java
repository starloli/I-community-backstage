package com.example.demo2.entity;

import com.example.demo2.enums.PackageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String recipientName;
    private String phoneNumber;
    private String unitNumber;
    
    @Column(nullable = false)
    private String trackingNumber;

    private String courier;

    @Column(nullable = false)
    private String arrivedAt;

    private String pickupAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PackageStatus status;

    private String notes;
    
    @Column(nullable = false)
    private Boolean notified;

    public Package(
        String recipientName,
        String phoneNumber,
        String unitNumber,
        String trackingNumber,
        String courier,
        String arrivedAt,
        String notes
    ) {
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.unitNumber = unitNumber;
        this.trackingNumber = trackingNumber;
        this.courier = courier;
        this.arrivedAt = arrivedAt;
        this.status = PackageStatus.WAITING;
        this.notes = notes;
        this.notified = false;
    }
}
