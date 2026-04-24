package com.example.demo2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.ModifyResidentRequset;
import com.example.demo2.dto.request.ResidentMyselfModifyRequest;
import com.example.demo2.dto.request.SuperAdminSelfRequest;
import com.example.demo2.dto.response.ModifyResidentResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserDao;
import com.example.demo2.service.ModifyResidentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor

@RequestMapping("/modify")
@CrossOrigin(origins = "http://localhost:4200")
public class ModifyController {

  @Autowired
  private ModifyResidentService modifyResidentService;

  @Autowired
  private UserDao userDao;

  // TODO: 【Phase 3】新增 AuthService 和 PasswordEncoder 注入
  // @Autowired
  // private AuthService authService;
  //
  // @Autowired
  // private PasswordEncoder passwordEncoder;

  @PostMapping("/superadmin/send-change-verify-code")
  public ResponseEntity<?> superadminSendChangeVerifyCode() {
    // TODO: 【Phase 3】發送驗證碼到舊信箱（確認修改前）
    // 功能：在點擊「儲存變更」時，發送驗證碼到現有信箱
    // 
    // 步驟：
    // 1. 從 JWT token 獲取當前用戶信息
    // 2. 調用 authService.generatePasswordChangeVerifyCode(currentUser.getEmail())
    // 3. 返回成功訊息和驗證碼有效期 (900 秒)
    //
    // 異常處理：
    // - 信箱不存在
    // - 郵件發送失敗
    return ResponseEntity.ok(Map.of("message", "已發送"));
  }

  @PostMapping("/superadmin/verify-password")
  public ResponseEntity<?> superadminVerifyPassword() {
    // TODO: 【Phase 3】驗證超級管理員舊密碼（進入編輯頁面前）
    // 功能：進入 user-info 頁面前，先驗證舊密碼
    //
    // 步驟：
    // 1. 接收 VerifyPasswordRequest { password }
    // 2. 從 JWT token 獲取當前用戶
    // 3. 調用 authService.verifyOldPassword(currentUser, request.password())
    // 4. 若驗證通過，返回 { message: "密碼驗證成功", valid: true }
    // 5. 若驗證失敗，返回 400 { message: "密碼不正確" }
    //
    // 異常處理：
    // - 無法辨識登入者
    // - 密碼驗證失敗
    return ResponseEntity.ok(Map.of("message", "驗證成功"));
  }

  @PutMapping("/superadmin/self")
  public ResponseEntity<?> superadminSelf(@RequestBody SuperAdminSelfRequest user) {
    // TODO: 【Phase 3】超級管理員更新自身資料（驗證碼驗證後執行）
    // 功能：驗證驗證碼後，執行所有更新（姓名、信箱、電話、密碼）
    //
    // 步驟：
    // 1. 從 JWT token 獲取當前用戶
    // 2. 調用 authService.verifyPasswordChangeCode(currentUser.getEmail(), user.verifyCode())
    // 3. 若驗證碼無效或過期，返回 400 { message: "驗證碼無效或已過期" }
    // 4. 若驗證通過，更新用戶資料：
    //    - fullName, email, phone
    //    - password（使用 passwordEncoder.encode()）
    // 5. 調用 userDao.save(currentUser)
    // 6. 返回成功訊息 { message: "資料已成功更新" }
    //
    // 異常處理：
    // - 驗證碼驗證失敗
    // - 資料更新失敗
    return ResponseEntity.ok(Map.of("message", "超級管理員變更成功"));
  }

  // 超級管理員去修改權限
  @PutMapping("/superadmin")
  public ResponseEntity<?> superAdminModifyResidentData(@RequestBody ModifyResidentRequset request) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      User superAdmin = userDao.findByUserName(authentication.getName())
          .orElseThrow(() -> new RuntimeException("無法辨識目前登入者"));

      // 修正傳入 ID
      modifyResidentService.superAdminModifyResidentData(request, superAdmin.getUserId());

      return ResponseEntity.ok(Map.of("message", "超級管理員：資料與權限更新成功"));
    } catch (RuntimeException e) {
      // 回傳 400 Bad Request 並帶上 Service 丟出的錯誤訊息
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/superadmin")
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> response = userDao.findAllUserExpectSuperadmin().stream()
        .map(UserResponse::from)
        .toList();
    return ResponseEntity.ok(response);
  }

  // 普通管理員修改用戶資料
  @PutMapping("/admin")
  public ResponseEntity<?> adminModifyResidentData(@Valid @RequestBody ModifyResidentRequset request) {
    try {
      modifyResidentService.modifyResidentData(request);
      return ResponseEntity.ok(Map.of("message", "管理員：資料與權限更新成功"));
    } catch (RuntimeException e) {

      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 住戶自己修改資料
  @PutMapping("/myself")
  public ResponseEntity<Map<String, String>> myself(@Valid @RequestBody ResidentMyselfModifyRequest request) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      User user = userDao.findByUserName(authentication.getName())
          .orElseThrow(() -> new RuntimeException("無法辨識目前登入者"));

      modifyResidentService.residentModifyOwnData(request, user.getUserId());
      return ResponseEntity.ok(Map.of("message", "個人基本信息修改成功"));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  // 管理員自己修改資料
  @PutMapping("/adminMyself")
  public ResponseEntity<?> adminMyself(@Valid @RequestBody User request) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      User user = userDao.findByUserName(authentication.getName())
          .orElseThrow(() -> new RuntimeException("無法辨識目前登入者"));
      modifyResidentService.adminModifyOwnData(request, user.getUserId());
      return ResponseEntity.ok(Map.of("message", "個人基本信息修改成功"));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  // 獲取全部人的資料
  @GetMapping("/getAllData")
  public ResponseEntity<List<ModifyResidentResponse>> getAllData() {
    List<User> users = userDao.findAll();
    List<ModifyResidentResponse> responses = users.stream()
        .map(ModifyResidentResponse::from) // 呼叫你 Record 裡的 static from 方法
        .toList();
    return ResponseEntity.ok(responses);
  }
}
