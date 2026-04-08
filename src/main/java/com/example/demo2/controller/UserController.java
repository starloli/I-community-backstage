package com.example.demo2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.response.PackageResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserDao;
import com.example.demo2.service.PackageService;
import com.example.demo2.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final PackageService packageService;
    private final UserService userService;
    
    @Autowired
    private UserDao userDao;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
        Authentication authentication
    ) {
        String name = authentication.getName();
        return ResponseEntity.ok(userService.getProfileByName(name));
    }

    @GetMapping("/package")
    public ResponseEntity<List<PackageResponse>> getMyPackages(
        Authentication authentication
    ) {
        String name = authentication.getName();
        return ResponseEntity.ok(packageService.searchByUser(name));
    }
    

}
