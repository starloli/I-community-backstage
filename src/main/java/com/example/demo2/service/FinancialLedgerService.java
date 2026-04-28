package com.example.demo2.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.ManualTransactionRequest;
import com.example.demo2.entity.FinancialLedger;
import com.example.demo2.entity.User;
import com.example.demo2.enums.FinancailLedgerCategory;
import com.example.demo2.enums.TransactionType;
import com.example.demo2.repository.FinancialLedgerDao;

@Service
public class FinancialLedgerService {

	@Autowired
    private FinancialLedgerDao ledgerDao;
	
	@Transactional
	public void recordTransaction(TransactionType type, BigDecimal amount, 
	                            FinancailLedgerCategory category, // 🌟 使用 Enum
	                            String remark, String module, 
	                            Long sourceId, User operator) {
	    
	    // 1. 獲取餘額邏輯同前...
	    BigDecimal currentBalance = ledgerDao.findTopByOrderByIdDesc()
	            .map(FinancialLedger::getBalance).orElse(BigDecimal.ZERO);

	    BigDecimal newBalance = (type == TransactionType.INCOME) 
	            ? currentBalance.add(amount) : currentBalance.subtract(amount);

	    // 2. 封裝 Entity
	    FinancialLedger ledger = new FinancialLedger();
	    ledger.setType(type);
	    ledger.setAmount(amount);
	    ledger.setBalance(newBalance);
	    ledger.setCategory(category); // 傳入 Enum
	    ledger.setRemark(remark);
	    ledger.setSourceModule(module);
	    ledger.setSourceId(sourceId);
	    ledger.setOperator(operator);

	    ledgerDao.save(ledger);
	}
	
	
	//手動輸入支出
	@Transactional
	public void recordManualTransaction(ManualTransactionRequest request,User creator) {
		  BigDecimal currentBalance = ledgerDao.findTopByOrderByIdDesc()
		            .map(FinancialLedger::getBalance).orElse(BigDecimal.ZERO);

		    BigDecimal newBalance = (request.getType() == TransactionType.INCOME) 
		            ? currentBalance.add(request.getAmount()) : currentBalance.subtract(request.getAmount());

		    // 2. 封裝 Entity
		    FinancialLedger ledger = new FinancialLedger();
		    ledger.setType(request.getType());
		    ledger.setAmount(request.getAmount());
		    ledger.setBalance(newBalance);
		    ledger.setCategory(request.getCategory()); // 傳入 Enum
		    ledger.setRemark(request.getRemark());
		    ledger.setSourceModule(request.getModule());
		    ledger.setSourceId(request.getSourceId());
		    ledger.setTransactionDate(request.getTransactionDate());
		    ledger.setOperator(creator);

		    ledgerDao.save(ledger);
		}
		
	

}
