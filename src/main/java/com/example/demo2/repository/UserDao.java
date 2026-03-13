package com.example.demo2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.User;

public interface UserDao extends JpaRepository<User, Integer>{

    Optional<User> findByUserName(String userName);
}
