package com.example.demo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.RepairRequest;

public interface RepairRequestDao extends JpaRepository<RepairRequest, Integer> {
    
}
