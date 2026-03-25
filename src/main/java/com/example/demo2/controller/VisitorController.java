package com.example.demo2.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.AllVisitorRequest;
import com.example.demo2.dto.request.VisitorRequest;
import com.example.demo2.dto.response.VisitorGetUserMassageResponse;
import com.example.demo2.dto.response.VisitorResponse;
import com.example.demo2.entity.User;
import com.example.demo2.entity.Visitor;
import com.example.demo2.enums.VisitorStatus;
import com.example.demo2.repository.UserDao;
import com.example.demo2.repository.VisitorDao;
import com.example.demo2.service.VisitorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor")
@CrossOrigin(origins = "http://localhost:4200")
public class VisitorController {
	  @Autowired
	    private VisitorService visitorService;
	
		@Autowired
	    private VisitorDao visitorDao;
	
	    @Autowired
	    private UserDao userDao;
	  
	 @PostMapping("/saveVisitor")
	public ResponseEntity<VisitorResponse> saveVistor( @RequestBody VisitorRequest visitorRequest){
	
		 VisitorResponse response = visitorService.saveVisitor(visitorRequest);	
		  return ResponseEntity.ok(response);
		
	}
	 //住戶端新增訪客 多了預約時間
	 @PostMapping("/user/save")
	 public ResponseEntity<VisitorResponse> saveByToken(@RequestBody VisitorRequest request, Authentication auth) {

	     return ResponseEntity.ok(visitorService.saveVisitorForUser(request, auth.getName()));
	 }
	 
	 
	 //得到全部訪客   多了預約時間
	 @GetMapping("/getVisitor")
	 public ResponseEntity<List<AllVisitorRequest>> getAllVisitors() {
	        List<AllVisitorRequest> list = visitorService.getAllVisitors();
	        return ResponseEntity.ok(list);
	    }
	


	 @PutMapping("/modifyVisitor/{id}")
	 public ResponseEntity<String> modifyVisitor(
	     @PathVariable("id") Integer id,  
	     @RequestBody VisitorRequest visitorRequest
	 ) {
	     visitorService.updateVisitor(id, visitorRequest);
	     return ResponseEntity.ok("訪客資料已更新成功");
	 }
	 //簽退 
	 @PutMapping("/checkOut/{id}") 
	 public ResponseEntity<Map<String, String>> checkOut(@PathVariable("id") Integer id) {
	     Visitor visitor = visitorDao.findById(id)
	             .orElseThrow(() -> new RuntimeException("找不到該筆記錄"));
	             
	   
	     visitor.setCheckOutTime(LocalDateTime.now());
	     visitor.setStatus(VisitorStatus.LEFT); 
	     
	     visitorDao.save(visitor);
	     return ResponseEntity.ok(Map.of("message", "簽退成功"));
	 }
	 
	 //入内 
	 @PutMapping("/inside/{id}") 
	 public ResponseEntity<Map<String, String>> inside(@PathVariable("id") Integer id) {
	     Visitor visitor = visitorDao.findById(id)
	             .orElseThrow(() -> new RuntimeException("找不到該筆記錄"));
	             
	   
	     visitor.setCheckInTime(LocalDateTime.now());
	     visitor.setStatus(VisitorStatus.INSIDE); 
	     
	     visitorDao.save(visitor);
	     return ResponseEntity.ok(Map.of("message", "入内成功"));
	 }
	 
	
//	 去得到該住戶門牌所有的住戶
	    @GetMapping("/by-address")
	    public ResponseEntity<List<VisitorGetUserMassageResponse>> getResidents(@RequestParam("address") String unitNumber) {
	    	List<User> residents = userDao.findByUnitNumber(unitNumber);
	        
	    	List<VisitorGetUserMassageResponse> response = visitorService.getResidentDataByAddress(unitNumber);
	        
	        return ResponseEntity.ok(response);
}
	    
//	    得到自己的token
	    @GetMapping("/my-visitors")
	    public ResponseEntity<List<AllVisitorRequest>> getMyToken(Authentication authentication){
	    	String name = authentication.getName(); 
	        
	        System.out.println("當前登入者是: " + name);
	      
	        // 接下來去 Service 查資料...
	        return ResponseEntity.ok(visitorService.getVisitorsByUserName(name));
	    }
	    
	    
	    //得到全部住戶的地址
	    @GetMapping("/allAddresses")
	    public List<String> getAllUniqueAddresses() {
	        // 使用 JPQL 的 DISTINCT 關鍵字，讓資料庫直接幫你完成去重，效率最高
	        return userDao.findDistinctUnitNumbers();
	    }
	    
	    
	    
}