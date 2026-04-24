package com.example.demo2.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo2.enums.FinancailLedgerCategory;
import com.example.demo2.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name="financial_ledger")
@Data
@NoArgsConstructor
public class FinancialLedger {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 交易時間（不一定是建立時間，例如可以補錄昨天的帳）
	@Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();
    //類型 支出/收入
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    
    @Column(nullable = false)
    private BigDecimal amount;    // 本次變動金額
    
    private BigDecimal balance;   //  變動後的即時結餘 
    @Enumerated(EnumType.STRING)
    private FinancailLedgerCategory category;      // 分類：薪資、管理費、維修費、水電公設
    
    private String remark;   // 備註：例如「A1 棟 4 樓水管維修支出」
    
    private String sourceModule;  // 來源模組：BILL, SALARY, REPAIR
 // 純數字 ID，記錄來源單據的主鍵
    private Long sourceId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    @JsonIgnoreProperties({
        "hibernateLazyInitializer", 
        "handler", 
        "passwordHash", 
        "email", 
        "phone", 
        "is_active",
        "squareFootage",
        "carParkingSpace",
        "motorParkingSpace",
        "unitNumber"
    })
    private User operator;        // 記錄是哪位管理員操作的

}
