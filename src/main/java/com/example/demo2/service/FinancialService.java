package com.example.demo2.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo2.dto.request.FinancialSummaryRequest;

import com.example.demo2.entity.FinancialLedger;
import com.example.demo2.enums.TransactionType;
import com.example.demo2.repository.CategorySummaryProjection;
import com.example.demo2.repository.FinancialLedgerDao;
import com.example.demo2.repository.UserDao;

@Service
public class FinancialService {
	@Autowired
    private FinancialLedgerDao ledgerDao;
	@Autowired 
	private UserDao userDao;

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
    //得到一年每個月的盈餘
    public List<FinancialSummaryRequest> getYearSummary(int year){
    	List<FinancialSummaryRequest> yearlyData = new ArrayList<>();
    	for(int i=1;i<=12;i++) {
    		FinancialSummaryRequest monthlyData=	this.getMonthlySummary(year,i);
    		yearlyData.add(monthlyData);
    	}
    	return yearlyData;
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

    //得到特定月份的明細
    public List<FinancialLedger> getSummaryMonth(int year,int month){
    	
    	return ledgerDao.findByYearAndMonth(year,month);
    }
    
    
    //住戶查看的明細 
    public List<CategorySummaryProjection>  residentsInspect(){

    	List<CategorySummaryProjection> list = ledgerDao.findDetailedMonthlyStats();
    	return list;
    }
}
