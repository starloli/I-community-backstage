package com.example.demo2.dto.request;

// TODO: 【Phase 1】超級管理員自身資料更新請求 DTO
// - verifyCode 字段用於驗證碼驗證（確認修改前）
// - 包含：fullName, email, phone, password, verifyCode
public record SuperAdminSelfRequest(
    String fullName,
    String email,
    String phone,
    String password,
    String verifyCode
) {
}
