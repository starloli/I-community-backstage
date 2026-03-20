package com.example.demo2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.AnnouncementCreateRequest;
import com.example.demo2.dto.request.LoginRequest;
import com.example.demo2.dto.response.AnnouncementResponse;
import com.example.demo2.dto.response.LoginResponse;
import com.example.demo2.service.AnnouncementService;
import com.example.demo2.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
    
        private final AuthService authService;
        private final AnnouncementService announcementService;

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                @Valid @RequestBody LoginRequest request
        ) {
                return ResponseEntity.ok(authService.loginAdmin(request));
        }

        @PostMapping("/announ")
        public ResponseEntity<AnnouncementResponse> postAnnouncement(
                @Valid @RequestBody AnnouncementCreateRequest request,
                Authentication authentication
        ) {
                String name = authentication.getName();
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(announcementService.CreateAnnouncement(request, name));
        }

        @PutMapping("/announ/{id}")
        public ResponseEntity<AnnouncementResponse> putById(
                @PathVariable("id") Integer id,
                @Valid @RequestBody AnnouncementCreateRequest request
        ) {
                return ResponseEntity.ok(announcementService.updateById(id, request));
        }

        @DeleteMapping("/announ/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
                announcementService.deleteById(id);
                return ResponseEntity.noContent().build();
        }
}
