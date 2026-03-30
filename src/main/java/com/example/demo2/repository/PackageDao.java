package com.example.demo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo2.entity.Package;
import com.example.demo2.enums.PackageStatus;

import java.util.List;


public interface PackageDao extends JpaRepository<Package, Integer> {
    
    List<Package> findByUnitNumber(String unitNumber);
    long countByStatus(PackageStatus status);
}
