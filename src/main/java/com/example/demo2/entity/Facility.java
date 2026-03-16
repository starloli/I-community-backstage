package com.example.demo2.entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "facilities")
@Data
@NoArgsConstructor
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer facilityId;

    @Column(nullable = false)
    private String name;

    private String description;

    private Integer capacity;

    private LocalTime openTime;

    private LocalTime closeTime;

    @Column(nullable = false)
    private boolean isAvailable;

    public Facility(
            String name,
            String description,
            Integer capacity,
            LocalTime openTime,
            LocalTime closeTime,
            boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isAvailable = isAvailable;
    }
}