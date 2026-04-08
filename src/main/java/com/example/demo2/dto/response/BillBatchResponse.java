package com.example.demo2.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import com.example.demo2.enums.BillStatus;
import lombok.Data;

@Data
public class BillBatchResponse {
    private String unitNumber;
    private LocalDate billingMonth;
    private String title;
    private LocalDate dueDate;
    private BillStatus status;
    private String remark;
    private String creator;
    

    private Map<String, BigDecimal> fees; 
    

    private BigDecimal totalAmount;
}