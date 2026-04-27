package com.example.demo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.Department;

public interface DepartmentDao extends JpaRepository<Department, Long> {

}
