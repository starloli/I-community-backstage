package com.example.demo2.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.example.demo2.enums.BillStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class BillRequset {
	@NotBlank(message = "unitNumber can not be null")
	private String unitNumber;
	@NotEmpty(message = "費用清單不能為空")
	private Map<String, BigDecimal> fees;
	@NotBlank(message ="標題不得爲空")
	private String title;
	
	@NotNull(message = "billingMonth can not be null")
	private LocalDate billingMonth;
	@NotNull(message = "dueDate can not be null")
	private LocalDate dueDate;

	private BillStatus status;
	    
	//付款日期
	    private LocalDateTime paidAtDate;
	    //付款方式
//	@NotBlank
	    private String paymentMethod;
	    //管理者備注
	    private String remark;

	    private String creator;
}
