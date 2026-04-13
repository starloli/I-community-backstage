package com.example.demo2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.EmailVerifyCode;

public interface CodeDao extends JpaRepository<EmailVerifyCode, Long> {
    
    List<EmailVerifyCode> findByEmailAndCodeAndUsed(String email, String code, boolean used);
}
