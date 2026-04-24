package com.example.demo2.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.Calendar;

public interface CalendarDao extends JpaRepository<Calendar, Integer> {
    
    List<Calendar> findByDateBetween(LocalDate start, LocalDate end);
}
