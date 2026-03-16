package com.example.demo2.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo2.dto.request.UserCreateRequest;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserDao userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreateRequest request) {
        User user = new User(
            request.userName(),
            passwordEncoder.encode(request.password()),
            request.fullName(),
            request.email(),
            request.phone(),
            request.unitNumber()
            
        );
        userRepository.save(user);
        return UserResponse.from(user);
    }

    public UserResponse getProfileByName(String name) {
        return UserResponse.from(getUser(name));
    }

    private User getUser(String name) {
        User user =  userRepository.findByUserName(name)
            .orElseThrow(() -> new RuntimeException("user not exists"));
        return user;
    }
}
