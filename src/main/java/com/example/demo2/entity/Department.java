package com.example.demo2.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo2.enums.FinancailLedgerCategory;
import com.example.demo2.enums.TransactionType;
import com.sun.istack.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="departments")
@Data
@NoArgsConstructor
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	  @Column(nullable = false)
	  private String name;
	  
	  //部門描述
	  
	  private String description;
}
