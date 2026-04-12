package com.example.demo2.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo2.enums.UserRole;

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
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false, unique = true)
    private String unitNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private Boolean is_active;

    private LocalDateTime created_at;

    // 住戶平方數
    @Column(precision = 10, scale = 2)
    private BigDecimal squareFootage;

    // 汽車停車位
    private Integer carParkingSpace = 0; // 汽車位編號 (或數量)

    private Integer motorParkingSpace = 0; // 機車位編號

    public User(
            String userName,
            String passwordHash,
            String fullName,
            String email,
            String phone,
            String unitNumber) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.unitNumber = unitNumber;
        this.role = UserRole.RESIDENT;
        this.is_active = true;
        this.created_at = LocalDateTime.now();
    }
}
