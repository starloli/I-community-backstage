package com.example.demo2.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo2.dto.request.LoginRequest;
import com.example.demo2.dto.response.LoginResponse;
import com.example.demo2.entity.User;
import com.example.demo2.exception.UnauthorizedException;
import com.example.demo2.repository.UserDao;
import com.example.demo2.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserDao userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        System.out.println("login test");
        User user = userRepository.findByUserName(request.userName())
                .filter(u -> passwordEncoder.matches(
                        request.password(), u.getPasswordHash()
                ))
                .orElseThrow(UnauthorizedException::new);

        String accessToken = jwtTokenProvider.createToken(
                user.getUserName(), user.getRole()
        );

        return new LoginResponse(accessToken);
    }
}
