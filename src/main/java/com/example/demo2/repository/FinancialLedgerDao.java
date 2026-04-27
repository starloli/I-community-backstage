package com.example.demo2.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo2.entity.FinancialLedger;
import com.example.demo2.enums.TransactionType;
public interface FinancialLedgerDao extends JpaRepository<FinancialLedger,Long> {

	Optional<FinancialLedger> findTopByOrderByIdDesc();
	
	
	@Query("SELECT SUM(f.amount) FROM FinancialLedger f WHERE f.type = :type AND f.transactionDate BETWEEN :start AND :end")
    BigDecimal sumAmountByTypeAndDate(@Param("type") TransactionType type, 
                                      @Param("start") LocalDateTime start, 
                                      @Param("end") LocalDateTime end);

    // 統計歷史至今的某種類型金額 (例如：統計開站至今總支出)
    @Query("SELECT SUM(f.amount) FROM FinancialLedger f WHERE f.type = :type")
    BigDecimal sumTotalAmountByType(@Param("type") TransactionType type);
    
    
    List<FinancialLedger> findAllByOrderByIdDesc();
    
}
