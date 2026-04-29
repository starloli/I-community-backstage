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
    
    @Query("SELECT f FROM FinancialLedger f WHERE YEAR(f.transactionDate) = :year AND MONTH(f.transactionDate) = :month ORDER BY f.transactionDate DESC")
    List<FinancialLedger> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
    
    
    
    //查找每個月的收入和支出
    @Query("SELECT YEAR(f.transactionDate) as year, " +
    	       "MONTH(f.transactionDate) as month, " +
    	       "f.type as type, " +
    	       "f.category as category, " +
    	       "SUM(f.amount) as totalAmount " +
    	       "FROM FinancialLedger f " +
    	       "GROUP BY YEAR(f.transactionDate), MONTH(f.transactionDate), f.type, f.category " +
    	       "ORDER BY YEAR(f.transactionDate) DESC, MONTH(f.transactionDate) DESC")
    	List<CategorySummaryProjection> findDetailedMonthlyStats();
    
}
