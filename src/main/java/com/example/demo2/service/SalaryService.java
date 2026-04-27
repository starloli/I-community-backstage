package com.example.demo2.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.entity.Department;
import com.example.demo2.entity.Employee;
import com.example.demo2.entity.SalaryRecord;
import com.example.demo2.entity.User;
import com.example.demo2.enums.FinancailLedgerCategory;
import com.example.demo2.enums.SalaryStatusEnum;
import com.example.demo2.enums.TransactionType;
import com.example.demo2.repository.DepartmentDao;
import com.example.demo2.repository.EmployeeDao;
import com.example.demo2.repository.SalaryRecordDao;

@Service
public class SalaryService {
	@Autowired
    private SalaryRecordDao salaryRecordDao;
	
	@Autowired
    private FinancialLedgerService financialLedgerService;
	@Autowired
    private EmployeeDao employeeDao;
	@Autowired
	private DepartmentDao departmentDao;
	
	@Transactional
    public void generateMonthlySalaries(Long departmentId, int year, int month) {
        LocalDate settlementMonth = LocalDate.of(year, month, 1);
        
        Department dept = departmentDao.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("錯誤：部門 (ID: " + departmentId + ") 不存在"));
        // 找出該部門所有員工
        List<Employee> employees = employeeDao.findByDepartmentId(departmentId);
        if (employees.isEmpty()) {
            throw new RuntimeException("提示：該部門目前沒有員工，無需產生薪資單");
        }
        
        for (Employee emp : employees) {
            // 檢查是否已經產生過，避免重複產生
        	boolean exists = salaryRecordDao.existsByEmployeeIdAndSettlementMonth(emp.getId(), settlementMonth);
            if (exists) {
                throw new RuntimeException("錯誤：" + emp.getName() + " 在 " + year + "-" + month + " 已有薪資單，請勿重複產生");
            }

            SalaryRecord record = new SalaryRecord();
            record.setEmployee(emp);
            record.setBaseSalary(emp.getDefaultBaseSalary()); // 自動帶入底薪模板
            record.setSettlementMonth(settlementMonth);
            record.setStatus(SalaryStatusEnum.PENDING);
            
            salaryRecordDao.save(record);
        }
    }
	
	@Transactional
    public void paySalary(Long recordId, User operator, BigDecimal bonus, BigDecimal deduction) {
		SalaryRecord record = salaryRecordDao.findById(recordId)
	            .orElseThrow(() -> new RuntimeException("找不到紀錄"));

	    // 1. 更新最新的獎金與扣款
	    record.setBonus(bonus);
	    record.setDeduction(deduction);
	    
	    // 2. 計算實發總額
	    BigDecimal finalAmount = record.getBaseSalary()
	                                   .add(record.getBonus())
	                                   .subtract(record.getDeduction());

	    // 3. 變更狀態
	    record.setStatus(SalaryStatusEnum.PAID);
	    record.setPaidAt(LocalDateTime.now());
	    
	    // 4. 連動財務流水帳
	    financialLedgerService.recordTransaction(
	        TransactionType.EXPENSE,
	        finalAmount, // 這裡是計算後的結果
	        FinancailLedgerCategory.SALARY,
	        "發放薪資：" + record.getEmployee().getName(),
	        "SALARY_MOD",
	        record.getId(),
	        operator
	    );
	}
}
