package com.example.demo2.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class BatchBillRequest {
//標題
	private String title;  
	//開始月份
	private String billingMonth;
	//結束日期
	private LocalDate dueDate;
	//備注
	private String remark;
	//各種費用
	private Map<String,BigDecimal> commonFees;
	//全部住戶
	private List<String> unitNumbers;
	
	//創建人
	private String creator;
}
