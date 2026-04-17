package com.example.demo2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.BatchBillRequest;
import com.example.demo2.dto.request.BillRequset;
import com.example.demo2.dto.response.BatchResultDto;
import com.example.demo2.dto.response.MonthlyBillDto;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserDao;
import com.example.demo2.service.BillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bills")
@CrossOrigin(origins = "http://localhost:4200")
public class BillController {

    @Autowired 
    private BillService billService;

@Autowired
private UserDao userDao;
    


////得到住戶平方數
@GetMapping("/details")
public ResponseEntity<UserResponse> getUserDetails(@Valid @RequestParam("unitNumber") String unitNumber) {
    return userDao.findByUnitNumber(unitNumber)
        .stream()
        .findFirst() // 取得該地址的第一位住戶資料
        .map(user -> {
            UserResponse res = UserResponse.from(user); 
            return ResponseEntity.ok(res);
        })
        .orElse(ResponseEntity.notFound().build());
}


@PostMapping("/toUnitNumber")
public ResponseEntity<MonthlyBillDto> toUnitNumber(@Valid @RequestBody BillRequset billRequset) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User creator = userDao.findByUserName(authentication.getName())
            .orElseThrow(() -> new RuntimeException("無法辨識目前登入者"));

    // 現在回傳的是 MonthlyBillDto (內含 detail 物件)
    MonthlyBillDto response = billService.sendMonthlyBill(billRequset, creator);
    return ResponseEntity.ok(response);
}

/**
 * 住戶獲取自己的帳單列表 (一個月份一筆)
 */
@GetMapping("/getMyBill")
public ResponseEntity<List<MonthlyBillDto>> getMyBills() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = userDao.findByUserName(auth.getName()).orElseThrow();
    
    List<MonthlyBillDto> responses = billService.getResidentMonthlyBills(user.getUnitNumber());
    return ResponseEntity.ok(responses);
}

/**
 * 管理員更新繳費狀態
 */
@PutMapping("/pay/admin/{id}")
public ResponseEntity<Map<String, String>> adminPayBill(@PathVariable("id") Integer id) {

    billService.putBillStatus(id);
    return ResponseEntity.ok(Map.of("message", "繳費狀態已更新"));
}

//住戶自己繳費
@PutMapping("/pay/user/{id}")
public ResponseEntity<Map<String, String>> userPayBill(@PathVariable("id") Integer id) {
	billService.userPayBillStatus(id);
    return ResponseEntity.ok(Map.of("message", "繳費狀態已更新"));
}


//管理員獲取全部住戶的賬單
@GetMapping("/getAllBills")
public  List<MonthlyBillDto> getAllBills(){

	return billService.getAllBills();
}

@PostMapping("/sendAllBills")
public ResponseEntity<BatchResultDto> sendAllbills(@RequestBody BatchBillRequest request){
	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    User creator = userDao.findByUserName(authentication.getName())
	            .orElseThrow(() -> new RuntimeException("無法辨識目前登入者"));
	    BatchResultDto list=billService.sendAllBillsToUnit(request,creator);
	return ResponseEntity.ok(list);
}


//得到全部有異常的住戶地址
@GetMapping("/getAbnormalUnits")
public ResponseEntity<List<String>> getAbnormalUnits(){
	List<User> abnormalUnits =userDao.findBySquareFootageIsNull();
	List<String> abnormalUnitNumbers = abnormalUnits.stream()
            .map(User::getUnitNumber) // 只取房號
            .distinct()               // 如果有重複，只留一個
            .toList();
	return ResponseEntity.ok(abnormalUnitNumbers);
}

//管理員查看本月是否已經有賬單了
@GetMapping("/findByMonth")
public ResponseEntity<Map<String,String>> findByMonth(@RequestParam("billingMonth") String billingMonth){

	Boolean month=billService.checkBillInThisMonth(billingMonth);
return month? ResponseEntity.ok(Map.of("message","本月已經有賬單了")):ResponseEntity.ok((Map.of("message","本月還沒有賬單")));
}



}