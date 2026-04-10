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
import com.example.demo2.dto.response.ModifyResidentResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserDao;
import com.example.demo2.service.ModifyResidentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

@RequestMapping("/modify")
@CrossOrigin(origins = "http://localhost:4200")
public class ModifyResidentController {

	@Autowired
	private ModifyResidentService modifyResidentService;

	@Autowired
	private UserDao userDao;

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

	// 普通管理員修改用戶資料
	@PutMapping("/admin")
	public ResponseEntity<?> adminModifyResidentData(@RequestBody ModifyResidentRequset request) {
		try {
			modifyResidentService.modifyResidentData(request);
			return ResponseEntity.ok(Map.of("message", "普通管理員：資料與權限更新成功"));
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
