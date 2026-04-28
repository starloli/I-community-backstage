package com.example.demo2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.ModifyResidentRequset;
import com.example.demo2.dto.request.ResidentMyselfModifyRequest;
import com.example.demo2.entity.User;
import com.example.demo2.enums.UserRole;
import com.example.demo2.repository.UserDao;

@Service
public class ModifyResidentService {

	@Autowired
	UserDao userDao;

	/**
	 * 1. 普通管理員修改住戶資料 (限制多)
	 */
	@Transactional
	public void modifyResidentData(ModifyResidentRequset request) {
		User targetUser = userDao.findById(request.getUserId() != null ? request.getUserId() : 0)
				.orElseThrow(() -> new RuntimeException("找不到該住戶，ID: " + request.getUserId()));

		// 普通管理員不能動到管理層
		if ((targetUser.getRole() == UserRole.ADMIN || targetUser.getRole() == UserRole.SUPER_ADMIN)
				&& request.getUserId() != targetUser.getUserId()) {
			throw new RuntimeException("權限不足：普通管理員無法修改管理員等級的帳號");
		}

		if (request.getRole() == UserRole.ADMIN || request.getRole() == UserRole.SUPER_ADMIN) {
			throw new RuntimeException("權限不足：您無法將住戶提升為管理員");
		}

		if (targetUser.getStatus() != request.getStatus()) {
			throw new RuntimeException("權限不足：普通管理員無法啟用或停用帳號");
		}

		// 執行行政與資產資料更新 (不包含 Role 和 isActive)
		updateGeneralInfoAndAssets(targetUser, request);
	}

	/**
	 * 2. 超級管理員修改資料 (全權限)
	 */
	@Transactional
	public void superAdminModifyResidentData(ModifyResidentRequset request, Integer currentAdminId) {
		User targetUser = userDao.findById(request.getUserId() != null ? request.getUserId() : 0)
				.orElseThrow(() -> new RuntimeException("找不到該住戶，ID: " + request.getUserId()));

		// 🛑 防自殺與超管互鬥判斷
		if (targetUser.getUserId().equals(currentAdminId)) {
			// 如果改的是自己，檢查是否在降級或停權
			if (request.getRole() != targetUser.getRole() || Boolean.FALSE.equals(request.getStatus())) {
				throw new RuntimeException("為了系統安全，您不能修改自己的權限等級或停用自己的帳號。");
			}
		} else if (targetUser.getRole() == UserRole.SUPER_ADMIN) {
			// 如果改的是別人，且對方也是超管
			throw new RuntimeException("您無權修改其他超級管理員的帳號狀態。");
		}

		// ✅ 超管可以改權限與狀態
		targetUser.setRole(request.getRole());
		targetUser.setStatus(request.getStatus());

		// ✅ 超管也需要執行行政與資產資料更新
		updateGeneralInfoAndAssets(targetUser, request);
	}

	/**
	 * 3. 抽取出的共用邏輯：負責更新個人資料與全戶資產同步
	 */
	private void updateGeneralInfoAndAssets(User targetUser, ModifyResidentRequset request) {
		String oldUnit = targetUser.getUnitNumber();
		String newUnit = request.getUnitNumber();

		// 遷戶邏輯：處理新地址資料一致性
		if (!oldUnit.equals(newUnit)) {
			List<User> newMembers = userDao.findByUnitNumber(newUnit);
			if (!newMembers.isEmpty()) {
				User existingOwner = newMembers.get(0);
				request.setSquareFootage(existingOwner.getSquareFootage());
				request.setCarParkingSpace(existingOwner.getCarParkingSpace());
				request.setMotorParkingSpace(existingOwner.getMotorParkingSpace());
			} else {
				request.setSquareFootage(targetUser.getSquareFootage());
				request.setCarParkingSpace(targetUser.getCarParkingSpace());
				request.setMotorParkingSpace(targetUser.getMotorParkingSpace());
			}
		}

		// 更新個人基礎資訊
		targetUser.setFullName(request.getFullName());
		targetUser.setEmail(request.getEmail());
		targetUser.setPhone(request.getPhone());
		targetUser.setUnitNumber(newUnit);
		userDao.save(targetUser);

		// 同步新地址全戶資產
		List<User> members = userDao.findByUnitNumber(newUnit);
		if (members.stream().noneMatch(u -> u.getUserId().equals(targetUser.getUserId()))) {
			members.add(targetUser);
		}

		for (User user : members) {
			user.setSquareFootage(request.getSquareFootage());
			user.setCarParkingSpace(request.getCarParkingSpace());
			user.setMotorParkingSpace(request.getMotorParkingSpace());
			if (!user.getUserId().equals(targetUser.getUserId())) {
				user.setUnitNumber(newUnit);
			}
		}

		userDao.saveAll(members);
	}

	// 使用者自己修改自己的資料
	public void residentModifyOwnData(ResidentMyselfModifyRequest request, Integer userId) {
		User user = userDao.findById(userId != null ? userId : 0)
				.orElseThrow(() -> new RuntimeException("找不到該住戶，ID: " + userId));

		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		userDao.save(user);
	}

	// 管理者自己修改自己的資料
	public void adminModifyOwnData(User request, Integer userId) {
		User user = userDao.findById(userId != null ? userId : 0)
				.orElseThrow(() -> new RuntimeException("找不到該住戶，ID: " + userId));

		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setFullName(request.getFullName());
		user.setUnitNumber(request.getUnitNumber());

		userDao.save(user);
	}

}