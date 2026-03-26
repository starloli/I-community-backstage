package com.example.demo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.RepairRequest;
import com.example.demo2.enums.RepairStatus;

public interface RepairRequestDao extends JpaRepository<RepairRequest, Integer> {
 
    long countByStatus(RepairStatus status);
}
