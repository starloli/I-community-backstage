package com.example.demo2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo2.dto.request.AllVisitorRequest;
import com.example.demo2.dto.request.VisitorRequest;
import com.example.demo2.dto.response.VisitorGetUserMassageResponse;
import com.example.demo2.dto.response.VisitorResponse;
import com.example.demo2.entity.User;
import com.example.demo2.entity.Visitor;
import com.example.demo2.repository.UserDao;
import com.example.demo2.repository.VisitorDao;

@Service
public class VisitorService {




	@Autowired
    private VisitorDao visitorDao;
    
    @Autowired
    private UserDao userDao;


  


    public VisitorResponse saveVisitor(VisitorRequest request) {
        // 1. 基本資料轉換
        Visitor visitor = new Visitor();
        visitor.setVisitorName(request.getVisitorName());
        visitor.setVisitorPhone(request.getVisitorPhone());
        visitor.setLicensePlate(request.getLicensePlate());
        visitor.setPurpose(request.getPurpose());
        visitor.setCheckInTime(request.getCheckInTime());
        visitor.setCheckOutTime(request.getCheckOutTime());
      
        
   
        visitor.setStatus(request.getStatus());
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            visitor.setRegisteredBy(auth.getName()); // 這裡拿到的就是 Token 裡的 sub (lisiAccount)
        } else {
            visitor.setRegisteredBy("SYSTEM"); // 備用方案，防止意外
        }

        // 3. 檢查並設置被訪住戶
        if (request.getHostUserId() == null) {
            throw new RuntimeException("Host User ID 不能為空");
        }

        // 根據你的 request 是 Long，這裡轉為 Integer 以符合 userDao
        Integer userId = request.getHostUserId().intValue(); 

        User host = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到 ID 為 " + userId + " 的用戶"));
        
        visitor.setHostUser(host);

        Visitor saved = visitorDao.save(visitor);
        return VisitorResponse.fromEntity(saved);
    }
    
    //使用者端的新增訪客
    public VisitorResponse saveVisitorForUser(VisitorRequest request, String username) {
        // 從 Token 換取 User 實體
        User host = userDao.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("使用者不存在: " + username));

        return processSave(request, host);
    }

    /**
     * 💡 提取公共的儲存邏輯，避免重複程式碼
     */
    private VisitorResponse processSave(VisitorRequest request, User host) {
        Visitor visitor = new Visitor();
        visitor.setVisitorName(request.getVisitorName());
        visitor.setVisitorPhone(request.getVisitorPhone());
        visitor.setLicensePlate(request.getLicensePlate());
        visitor.setPurpose(request.getPurpose());
        visitor.setEstimatedTime(request.getEstimatedTime());
        visitor.setCheckInTime(request.getCheckInTime());
        visitor.setCheckOutTime(request.getCheckOutTime());
        
        // 如果是住戶自己登記，可以預設 registeredBy 為住戶姓名
        visitor.setRegisteredBy(request.getRegisteredBy() != null ? 
                                request.getRegisteredBy() : host.getFullName());
                                
        visitor.setStatus(request.getStatus());
        visitor.setHostUser(host);

        Visitor saved = visitorDao.save(visitor);
        return VisitorResponse.fromEntity(saved);
    }

    
    
    
    
    public List<AllVisitorRequest> getAllVisitors() {
        //  從資料庫取得所有訪客
        List<Visitor> visitors = visitorDao.findAll();

        return visitors.stream().map(visitor -> {
        	AllVisitorRequest dto = new AllVisitorRequest();
        	dto.setVisitorId(visitor.getVisitorId());
        	dto.setVisitorName(visitor.getVisitorName());
        	dto.setVisitorPhone(visitor.getVisitorPhone());
        	dto.setEstimatedTime(visitor.getEstimatedTime());
            dto.setCheckInTime(visitor.getCheckInTime());
            dto.setCheckOutTime(visitor.getCheckOutTime());
            dto.setLicensePlate(visitor.getLicensePlate());
            dto.setPurpose(visitor.getPurpose());
            dto.setRegisteredBy(visitor.getRegisteredBy());
            dto.setStatus(visitor.getStatus());
            if (visitor.getHostUser() != null) {
                dto.setResidentName(visitor.getHostUser().getFullName());
                dto.setResidentialAddress(visitor.getHostUser().getUnitNumber());
            }return dto;}).collect(Collectors.toList());
        }
    
    
    public void updateVisitor(Integer id, VisitorRequest request) {
        // 檢查該訪客記錄是否存在
        Visitor visitor = visitorDao.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到 ID 為 " + id + " 的訪客記錄"));


        visitor.setVisitorName(request.getVisitorName());
        visitor.setVisitorPhone(request.getVisitorPhone());
        visitor.setLicensePlate(request.getLicensePlate());
        visitor.setPurpose(request.getPurpose());
        visitor.setCheckInTime(request.getCheckInTime());
        visitor.setCheckOutTime(request.getCheckOutTime());
        visitor.setStatus(request.getStatus());


        if (request.getHostUserId() != null) {
            User host = userDao.findById(request.getHostUserId().intValue())
                    .orElseThrow(() -> new RuntimeException("找不到住戶 ID: " + request.getHostUserId()));
            visitor.setHostUser(host);
        }

        //  儲存更新後的 Entity
        visitorDao.save(visitor);
    }
    
    
    
    

    //名字模糊處理
    private String maskName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        int len = fullName.length();
        

        if (len == 2) {
            return fullName.substring(0, 1) + "*";
        } 

        else {
            return fullName.substring(0, 1) + "*" + fullName.substring(len - 1);
        }
    }
    
    public List<VisitorGetUserMassageResponse> getResidentDataByAddress(String unitNumber) {
        // 1. 從資料庫查詢原始資料
        List<User> residents = userDao.findByUnitNumber(unitNumber);
        
        // 2. 將 Entity 轉換為乾淨的 Response DTO
        return residents.stream()
            .map(user -> {
                VisitorGetUserMassageResponse dto = new VisitorGetUserMassageResponse();
                dto.setUserId(user.getUserId());
                String maskedName = maskName(user.getFullName());
                dto.setFullName(maskedName);
                dto.setPhone(user.getPhone());
                dto.setUnitNumber(user.getUnitNumber());
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    
	    
	    public List<AllVisitorRequest> getVisitorsByUserName (String userName) {
	    	User user = userDao.findByUserName(userName)
	                .orElseThrow(() -> new RuntimeException("使用者不存在: " + userName));
	
	
	        String myUnitNumber = user.getUnitNumber();
	        
	        // 3. 去訪客資料表找所有目的地是這個門牌的記錄
	        // 假設你的 VisitorDao 有 findByUnitNumber 方法
	        List<Visitor> visitors = visitorDao.findByHostUser_UnitNumber(myUnitNumber);
	        
	        return visitors.stream().map(v -> {
	            AllVisitorRequest dto = new AllVisitorRequest();
	            
	            // --- 訪客基本資訊 ---
	            dto.setVisitorId(v.getVisitorId()); 
	            dto.setVisitorName(v.getVisitorName());
	            dto.setVisitorPhone(v.getVisitorPhone());
	            dto.setPurpose(v.getPurpose());
	            dto.setLicensePlate(v.getLicensePlate());
	            
	            // --- 時間與狀態 ---
	            dto.setEstimatedTime(v.getEstimatedTime());
	            dto.setCheckInTime(v.getCheckInTime());
	            dto.setCheckOutTime(v.getCheckOutTime());
	            dto.setStatus(v.getStatus()); // Enum 直接對應
	            
	            // --- 關聯資訊 ---
	            // 假設 Visitor Entity 裡存的欄位名稱叫 registeredBy
	            dto.setRegisteredBy(v.getRegisteredBy()); 
	            
	            // 這裡直接帶入剛才查到的住戶資訊
	            dto.setResidentName(user.getUserName());
	            dto.setResidentialAddress(v.getHostUser().getUnitNumber()); 
	            
	            return dto;
	        }).collect(Collectors.toList());
	    }
	}
	
	
