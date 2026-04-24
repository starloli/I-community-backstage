package com.example.demo2.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSummaryRequest {
	private BigDecimal totalIncome;    // 總收入
    private BigDecimal totalExpense;   // 總支出
    private BigDecimal balance;        // 盈餘 (收入 - 支出)
}
