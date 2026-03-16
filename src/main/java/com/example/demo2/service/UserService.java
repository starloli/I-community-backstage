package com.example.demo2.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo2.dto.request.FacilityRegistRequest;
import com.example.demo2.dto.request.UserCreateRequest;
import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.entity.Facility;
import com.example.demo2.entity.User;
import com.example.demo2.exception.NotFoundException;
import com.example.demo2.repository.FacilityDao;
import com.example.demo2.repository.UserDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userRepository;
    private final FacilityDao facilityRepository;
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

    public FacilityResponse registFacility(FacilityRegistRequest request) {
        Facility facility = new Facility(
                request.name(),
                request.description(),
                request.capacity(),
                request.openTime(),
                request.closeTime(),
                request.isAvailable()
        );
        facilityRepository.save(facility);
        return FacilityResponse.from(facility);
    }

    public UserResponse getProfileByName(String name) {
        return UserResponse.from(getUser(name));
    }

    private User getUser(String name) {
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new NotFoundException("user not exists"));
        return user;
    }
}
