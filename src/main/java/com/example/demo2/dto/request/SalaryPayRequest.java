package com.example.demo2.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SalaryPayRequest {
    private BigDecimal bonus;     // 獎金
    private BigDecimal deduction; // 扣款
}