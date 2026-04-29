package com.example.demo2.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.FinancialSummaryRequest;
import com.example.demo2.dto.request.ManualTransactionRequest;
import com.example.demo2.dto.request.SalaryGenerateRequest;
import com.example.demo2.dto.request.SalaryPayRequest;
import com.example.demo2.entity.Department;
import com.example.demo2.entity.Employee;
import com.example.demo2.entity.FinancialLedger;
import com.example.demo2.entity.SalaryRecord;
import com.example.demo2.entity.User;
import com.example.demo2.repository.CategorySummaryProjection;
import com.example.demo2.repository.DepartmentDao;
import com.example.demo2.repository.EmployeeDao;
import com.example.demo2.repository.SalaryRecordDao;
import com.example.demo2.repository.UserDao;
import com.example.demo2.service.FinancialLedgerService;
import com.example.demo2.service.FinancialService;
import com.example.demo2.service.SalaryService;

import jakarta.validation.Valid;

@RestController

@RequestMapping("/Salary")
@CrossOrigin(origins = "http://localhost:4200")
public class SalaryController {

	@Autowired
    private  UserController userController;
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
    private EmployeeDao employeeDao;

	@Autowired
    private SalaryService salaryService;

    @Autowired
    private SalaryRecordDao salaryRecordDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FinancialService financialService;
    @Autowired
    private FinancialLedgerService financialLedgerService;


    //新增一個部門
    @PostMapping("/createDeparment")
    public ResponseEntity<Map<String, String>> createDeparment(@RequestBody Department department){
    	Map<String, String> response = new HashMap<>();
        try {
            // 1. 執行儲存
            departmentDao.save(department);
            
            // 2. 準備成功的訊息
            response.put("status", "success");
            response.put("message", "部門「" + department.getName() + "」新增成功");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // 3. 處理失敗情況
            response.put("status", "error");
            response.put("message", "新增失敗：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
	//找出全部員工
    @GetMapping("/getAllEmplyee")
    public List<Employee> getAll() {
        return employeeDao.findAll();
    }
    
  

    //新建一個員工
    @PostMapping("/create/Employee")
    public Employee create(@RequestBody Employee employee) {
        return employeeDao.save(employee);
    }

    // 根據部門篩選員工 
    @GetMapping("/department/{deptId}")
    public List<Employee> getByDept(@PathVariable("deptId") Long deptId) {
        return employeeDao.findByDepartmentId(deptId);
    }
    
 //  產生薪資單 (傳入部門 ID、年份、月份)
    @PostMapping("/generate")
    public String generate(@RequestBody SalaryGenerateRequest salaryGenerateRequest) {
        salaryService.generateMonthlySalaries(salaryGenerateRequest.getDepartmentId(), salaryGenerateRequest.getYear(),
        		salaryGenerateRequest.getMonth());
        return "薪資單已生成";
    }

    // 查詢特定月份的薪資紀錄 (顯示在 Angular 列表)
    @GetMapping("/month/{date}")
    public List<SalaryRecord> getByMonth(@PathVariable("date") String date) {
    	String fullDate = date;
        if (date.length() == 7) { // 只有 yyyy-MM
            fullDate = date + "-01";
        }
        LocalDate month = LocalDate.parse(fullDate);
        return salaryRecordDao.findBySettlementMonth(month);
    }

    //  執行發放薪資 (連動財務流水帳)
    @PutMapping("/pay/{recordId}")
    public String pay(@PathVariable("recordId") Long recordId,@RequestBody SalaryPayRequest request) {
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	    User creator = userDao.findByUserName(authentication.getName())
    	        .orElseThrow(() -> new RuntimeException("無法辨識目前登入者"));
    	    salaryService.paySalary(recordId, creator, request.getBonus(), request.getDeduction());
        return "薪資發放成功，已計入財務總帳";
    }
    
    //得到全部部門
    @GetMapping("/getDepartment")
    public List<Department> getDepartments(){
    	return departmentDao.findAll();
    }
    
    
    
    
    
    
//    得到特定月份的盈餘
    @GetMapping("/summary/{year}/{month}")
    public FinancialSummaryRequest getMonthly(@PathVariable("year") int year, @PathVariable("month") int month) {
        return financialService.getMonthlySummary(year, month);
    }
    //得到特定月份的明細
    @GetMapping("summary/month/{year}/{month}")
    public List<FinancialLedger> summaryMonth(@PathVariable("year") int year, @PathVariable("month") int month){

    	return     	financialService.getSummaryMonth(year,month);
    	
    }
    
    
    //得到特定年份的中全部月份的盈餘
    @GetMapping("/summary/{year}")
    public List<FinancialSummaryRequest> getYearSummary(@PathVariable("year") int year){
    	
    	return financialService.getYearSummary(year);
    }
    
    
//    / 獲取目前總資產狀態
    @GetMapping("/summary/total")
    public FinancialSummaryRequest getTotal() {
        return financialService.getTotalSummary();
    }
    //支出收入明細
    @GetMapping("/summary/now")
    public List<FinancialLedger> summaryNow(){
    	return financialService.getAllTransactions();
    }
    
    //手動輸入支出或者收入
    @PostMapping("/recordManualTransaction")
    public ResponseEntity<Map<String,String>> recordManualTransaction( @Valid @RequestBody	ManualTransactionRequest request) {
    	Map<String, String> response = new HashMap<>();
    
    	try {
    	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	    User creator = userDao.findByUserName(authentication.getName())
    	        .orElseThrow(() -> new RuntimeException("無法辨識目前登入者"));
    	    this.financialLedgerService.recordManualTransaction(request,creator);
    	    response.put("新增成功","ok");
    return ResponseEntity.ok(response);
    	}catch (Exception e) {
    	    response.put("message", "新增失敗：" + e.getMessage());
    	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    	    		}
}
    
    //住戶查看的明細  是分組后的資料
    
    @GetMapping("/ResidentsInspect")
    public ResponseEntity<List<CategorySummaryProjection>> ResidentsInspect(){
  
    	return   	ResponseEntity.ok(financialService.residentsInspect());
    }
}
