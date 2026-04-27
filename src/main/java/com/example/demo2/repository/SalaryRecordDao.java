package com.example.demo2.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.SalaryRecord;
import com.example.demo2.enums.SalaryStatusEnum;

public interface SalaryRecordDao extends JpaRepository<SalaryRecord,Long>{
	//  檢查特定員工在特定月份是否已有紀錄 (防止重複生成)
    boolean existsByEmployeeIdAndSettlementMonth(Long employeeId, LocalDate month);

    // 找出特定月份的所有薪資紀錄 (方便管理員查看整個月的發薪表)
    List<SalaryRecord> findBySettlementMonth(LocalDate month);

    // 找出特定狀態的紀錄 (例如：找出所有 PENDING 尚未發放的人)
    List<SalaryRecord> findByStatus(SalaryStatusEnum status);
}
