package com.example.demo2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.Employee;

public interface EmployeeDao extends JpaRepository<Employee,Long>{

	
List<Employee> findByDepartmentId(Long departmentId);
    

    
}
