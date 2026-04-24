package com.example.demo2.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo2.dto.request.FinancialSummaryRequest;
import com.example.demo2.entity.FinancialLedger;
import com.example.demo2.enums.TransactionType;
import com.example.demo2.repository.FinancialLedgerDao;

@Service
public class FinancialService {
	@Autowired
    private FinancialLedgerDao ledgerDao;

    // 獲取本月盈餘
    public FinancialSummaryRequest getMonthlySummary(int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusNanos(1);

        BigDecimal income = ledgerDao.sumAmountByTypeAndDate(TransactionType.INCOME, start, end);
        BigDecimal expense = ledgerDao.sumAmountByTypeAndDate(TransactionType.EXPENSE, start, end);

        // 處理 null 值（如果該月沒資料，SUM 會回傳 null）
        income = (income != null) ? income : BigDecimal.ZERO;
        expense = (expense != null) ? expense : BigDecimal.ZERO;

        return new FinancialSummaryRequest(income, expense, income.subtract(expense));
    }

    // 獲取目前總盈餘 (開站至今)
    public FinancialSummaryRequest getTotalSummary() {
        BigDecimal income = ledgerDao.sumTotalAmountByType(TransactionType.INCOME);
        BigDecimal expense = ledgerDao.sumTotalAmountByType(TransactionType.EXPENSE);

        income = (income != null) ? income : BigDecimal.ZERO;
        expense = (expense != null) ? expense : BigDecimal.ZERO;

        return new FinancialSummaryRequest(income, expense, income.subtract(expense));
    }


    //得到全部的明細
    public List<FinancialLedger> getAllTransactions() {
        return ledgerDao.findAllByOrderByIdDesc();
    }


}
