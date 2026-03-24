package com.example.demo2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo2.entity.Visitor;

@Repository
public interface VisitorDao extends JpaRepository<Visitor, Integer>{
	List<Visitor> findByHostUser_UnitNumber(String unitNumber);
	List<Visitor> findByHostUser_UserName (String userName);

}
