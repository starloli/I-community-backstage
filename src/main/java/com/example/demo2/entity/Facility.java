package com.example.demo2.entity;

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

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String openTime;

    @Column(nullable = false)
    private String closeTime;

    @Column(nullable = false)
    private boolean isReservable;

    @Column(nullable = false)
    private boolean isAvailable;

    public Facility(
            String name,
            String description,
            Integer capacity,
            String openTime,
            String closeTime,
            boolean isReservable,
            boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isReservable = isReservable;
        this.isAvailable = isAvailable;
    }
}