package com.example.demo2.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo2.entity.Bill;
import com.example.demo2.enums.BillStatus;
import com.example.demo2.enums.BillType;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BillResponse {
	@NotBlank(message ="住戶不能為空")
	private String unitNumber;
	
	 private BillType billType;
	 private BigDecimal amount;
	  private LocalDate billingMonth;
	   private LocalDate dueDate;
	    private BillStatus status;
	    private String title;
	    //付款日期
	    private LocalDateTime paidAtDate;
	    //付款方式
	    private String paymentMethod;
	    //管理者備注
	    private String remark;

	    private String creator;
	    
	    
	    public static BillResponse fromEntity(Bill bill) {
	    	BillResponse billresponse= new BillResponse();

	    	billresponse.setUnitNumber(bill.getUnitNumber());
	    	billresponse.setBillType(bill.getBillType());
	    	billresponse.setAmount(bill.getAmount());
	    	billresponse.setTitle(bill.getTitle());
	    	billresponse.setBillingMonth(bill.getBillingMonth());
	    	billresponse.setDueDate(bill.getDueDate());
	    	billresponse.setStatus(bill.getStatus());
	    	billresponse.setPaidAtDate(bill.getPaidAtDate());
	    	billresponse.setPaymentMethod(bill.getPaymentMethod().name());
	    	billresponse.setRemark(bill.getRemark());

	    	billresponse.setCreator(bill.getCreator().getFullName());
	    	if (bill.getStatus() == BillStatus.UNPAID && 
	    	        bill.getDueDate() != null && 
	    	        LocalDate.now().isAfter(bill.getDueDate())) {
	    	        
	    	        billresponse.setStatus(BillStatus.OVERDUE); // 設定為逾期
	    	    } else {
	    	        billresponse.setStatus(bill.getStatus());   // 維持原始狀態 (已繳或待繳)
	    	    }
	    	return billresponse;
}


	
}



