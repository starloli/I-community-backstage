package com.example.demo2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo2.entity.User;

public interface UserDao extends JpaRepository<User, Integer>{

    Optional<User> findByUserName(String userName);
    List<User> findByUnitNumber(String address);
    @Query("SELECT DISTINCT u.unitNumber FROM User u")
    List<String> findDistinctUnitNumbers();
    Optional<User> findByEmail(String email);
}

