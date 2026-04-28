package com.example.demo2.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo2.enums.FinancailLedgerCategory;
import com.example.demo2.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ManualTransactionRequest {

	//收入還是支出
	@NotNull
	private TransactionType type;
	@NotNull
	@Positive
	private BigDecimal amount;
	//收入或者支出的類型
	@NotNull
	private FinancailLedgerCategory category;
//備注
    private String remark;
    
    //時間
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime transactionDate;
    
    private String module="手動輸入";
    private Long sourceId=-1L;
    
    

}
