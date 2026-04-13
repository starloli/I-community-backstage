package com.example.demo2.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo2.entity.Bill;
import com.example.demo2.enums.BillStatus;
import com.example.demo2.enums.paymentMethodEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MonthlyBillDto {
	@NotBlank(message ="住戶不能為空")
	private String unitNumber;
    private String billingMonth;
    private BigDecimal totalAmount;
    private Detail detail;
private String dueDate;
private String title;
private paymentMethodEnum paymentMethod;
private LocalDateTime paidAtDate;
private String remark;
private String creator;
private Integer id;
private BillStatus status;
    @Data
    @AllArgsConstructor
    public static class Detail {
        private BigDecimal waterFee;
        private BigDecimal electricityFee;
        private BigDecimal managementFee;
        private BigDecimal carParkingFee; 
        private  BigDecimal locomotiveParkingFee;
        private BigDecimal otherFee;
    }
    
    
    public static MonthlyBillDto fromEntity(Bill bill) {
        MonthlyBillDto dto = new MonthlyBillDto();
        dto.setUnitNumber(bill.getUnitNumber());
        dto.setBillingMonth(bill.getBillingMonth().toString()); // 轉為 "2026-03"
        dto.setTotalAmount(bill.getAmount());
        dto.setDueDate(bill.getDueDate().toString());
        dto.setPaymentMethod(bill.getPaymentMethod());
        dto.setPaidAtDate(bill.getPaidAtDate());
        dto.setRemark(bill.getRemark());
         dto.setTitle(bill.getTitle());
         dto.setId(bill.getBillId());
        // 獲取建立者名稱 (避免傳回整個 User 物件，只傳名字)
        if (bill.getCreator() != null) {
            dto.setCreator(bill.getCreator().getFullName());
        }
     	if (bill.getStatus() == BillStatus.UNPAID && 
    	        bill.getDueDate() != null && 
    	        LocalDate.now().isAfter(bill.getDueDate())) {
    	        
     		dto.setStatus(BillStatus.OVERDUE); // 設定為逾期
    	    } else {
    	    	dto.setStatus(bill.getStatus());   // 維持原始狀態 (已繳或待繳)
    	    }
   

        // 組裝 Detail 內部類別
        dto.setDetail(new MonthlyBillDto.Detail(
            bill.getWaterFee(),
            bill.getElectricityFee(),
            bill.getManagementFee(),
            bill.getCarParkingFee(),
            bill.getLocomotiveParkingFee(),
            bill.getOtherFee()
        ));

        return dto;
    }
}