package com.example.demo2.dto.request;

public record UserCreateRequest(

        String userName,

        String password,

        String fullName,

        String email,

        String phone,

        String unitNumber) {
}
