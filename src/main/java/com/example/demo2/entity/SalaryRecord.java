package com.example.demo2.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo2.enums.SalaryStatusEnum;
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
@Table(name="salary_record")
@Data
@NoArgsConstructor
public class SalaryRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//發給哪一個
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Employee employee;

  
	// 薪資結算月份 
		@Column(nullable = false)
		private LocalDate settlementMonth;
		
		// 底薪 (產生時從 Employee.defaultBaseSalary 帶入，但允許手動微調)
		@Column(nullable = false, precision = 15, scale = 0)
		private BigDecimal baseSalary;
	    
		
		// 獎金 (台幣整數)
		@Column(nullable = false, precision = 15, scale = 0)
		private BigDecimal bonus = BigDecimal.ZERO;
		
		// 扣款 (台幣整數)
		@Column(nullable = false, precision = 15, scale = 0)
		private BigDecimal deduction = BigDecimal.ZERO;
		
		@Column(nullable = false)
		@Enumerated(EnumType.STRING)
		private SalaryStatusEnum status = SalaryStatusEnum.PENDING;
		
		// 實際發放時間
		private LocalDateTime paidAt;
		
		// 🌟 便利方法：計算實發總額
		public BigDecimal calculateTotalAmount() {
		    return baseSalary.add(bonus).subtract(deduction);
		    }
}
