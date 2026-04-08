package com.example.demo2.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.Announcement;
import com.example.demo2.entity.Bill;
import com.example.demo2.enums.BillType;

public interface BillDao  extends JpaRepository<Bill, Integer>{
	List<Bill>findByUnitNumber(String unitNumber);
	boolean existsByUnitNumberAndBillingMonthAndBillType(
	        String unitNumber, 
	        LocalDate billingMonth, 
	        BillType billType
	    );
	List<Bill> findByUnitNumberAndBillingMonth(String unitNumber, LocalDate billingMonth);
	
	
	boolean existsByUnitNumberAndBillingMonth(String unitNumber, LocalDate billingMonth);
}
