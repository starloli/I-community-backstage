package com.example.demo2.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo2.enums.BillStatus;
import com.example.demo2.enums.BillType;
import com.example.demo2.enums.paymentMethodEnum;

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
@Table(name = "bills")
@Data
@NoArgsConstructor
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;


    
    @Column(nullable = false)
    private String unitNumber;

    @Column(nullable =false)
    private String title;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BillType billType;

    @Column(nullable = false)
    private BigDecimal amount;
    

    @Column(nullable = false)
    private LocalDate billingMonth;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BillStatus status = BillStatus.UNPAID;

    private LocalDateTime paidAtDate;

    private paymentMethodEnum paymentMethod;

    @Column(nullable = false, updatable = false)
    private LocalDateTime pcreatedAt = LocalDateTime.now();
    @Column(length = 500) // 備註可以長一點
    private String remark;

    @Column(nullable = false)
    private BigDecimal waterFee;        // 水費分攤

    @Column(nullable = false)
    private BigDecimal electricityFee;  // 電費分攤

    @Column(nullable = false)
    private BigDecimal managementFee;   // 坪數管理費

    @Column(nullable = false)
    private BigDecimal carParkingFee;      // 汽車位費總計

    @Column(nullable = false)
    private BigDecimal locomotiveParkingFee; 
    
    @Column(nullable = false)
    private BigDecimal otherFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id",nullable = false) // 在資料庫產生 creator_id 外鍵
    private User creator;
    
 

    // 更新建構子（如果需要手動新增時帶入備註）
    public Bill(String unitNumber, String title, LocalDate billingMonth, LocalDate dueDate, 
            BigDecimal water, BigDecimal elec, BigDecimal mgmt, BigDecimal car, BigDecimal otherFee,
            BigDecimal motor, String remark, User creator) {
    this.unitNumber = unitNumber;
    this.title = title;
    this.billingMonth = billingMonth;
    this.dueDate = dueDate;
    this.remark = remark;
    this.creator = creator;
    this.otherFee=otherFee;
    // 賦值細項
    this.waterFee = water;
    this.electricityFee = elec;
    this.managementFee = mgmt;
    this.carParkingFee = car;
    this.locomotiveParkingFee = motor;
  
    
    // 重要：這筆帳單的性質是綜合管理費
    this.billType = BillType.MANAGEMENTFEE; 
    
    // 自動加總存入 amount 欄位
    this.amount = water.add(elec).add(mgmt).add(car).add(motor);
    
    this.status = BillStatus.UNPAID;
    this.pcreatedAt = LocalDateTime.now();
    }
}
