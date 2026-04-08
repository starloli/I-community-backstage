package com.example.demo2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.Facility;

public interface FacilityDao extends JpaRepository<Facility, Integer>{
    
    Optional<Facility> findByName(String name);
}

