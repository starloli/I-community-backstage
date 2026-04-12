package com.example.demo2.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.UserCreateRequest;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.entity.User;
import com.example.demo2.enums.UserRole;
import com.example.demo2.exception.NotFoundException;
import com.example.demo2.repository.UserDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = new User(
                request.userName(),
                passwordEncoder.encode(request.password()),
                request.fullName(),
                request.email(),
                request.phone(),
                request.unitNumber());
        userRepository.save(user);
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getProfileByName(String name) {
        return UserResponse.from(getUser(name));
    }

    @Transactional(readOnly = true)
    public long getTotalNumber() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllResidentUsers() {
        List<UserResponse> users = userRepository.findByIsActiveAndRole(true, UserRole.RESIDENT).stream()
                .map(UserResponse::from).toList();
        return users;
    }

    @Transactional(readOnly = true)
    private User getUser(String name) {
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new NotFoundException("user not exists"));
        return user;
    }
}
