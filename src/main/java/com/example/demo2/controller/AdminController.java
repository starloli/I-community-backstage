package com.example.demo2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.dto.request.AnnouncementCreateRequest;
import com.example.demo2.dto.request.LoginRequest;
import com.example.demo2.dto.request.PackageRequest;
import com.example.demo2.dto.request.RepairUpdateRequest;
import com.example.demo2.dto.response.AnnouncementResponse;
import com.example.demo2.dto.response.LoginResponse;
import com.example.demo2.dto.response.PackageResponse;
import com.example.demo2.dto.response.RepairResponse;
import com.example.demo2.dto.response.UserResponse;
import com.example.demo2.service.AnnouncementService;
import com.example.demo2.service.AuthService;
import com.example.demo2.service.PackageService;
import com.example.demo2.service.RepairRequestService;
import com.example.demo2.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

        private final AuthService authService;
        private final AnnouncementService announcementService;
        private final PackageService packageService;
        private final RepairRequestService repairRequestService;
        private final UserService userService;


        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                        @Valid @RequestBody LoginRequest request) {
                return ResponseEntity.ok(authService.loginAdmin(request));
        }

        @PostMapping("/announ")
        public ResponseEntity<AnnouncementResponse> postAnnouncement(
                        @Valid @RequestBody AnnouncementCreateRequest request,
                        Authentication authentication) {
                String name = authentication.getName();
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(announcementService.CreateAnnouncement(request, name));
        }

        @PutMapping("/announ/{id}")
        public ResponseEntity<AnnouncementResponse> putById(
                        @PathVariable("id") Integer id,
                        @Valid @RequestBody AnnouncementCreateRequest request) {
                return ResponseEntity.ok(announcementService.updateById(id, request));
        }

        @DeleteMapping("/announ/{id}")
        public ResponseEntity<Void> deleteAnnounById(
                        @PathVariable("id") Integer id) {
                announcementService.deleteById(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/package")
        public ResponseEntity<List<PackageResponse>> getAll() {
                return ResponseEntity.ok(packageService.searchAll());
        }

        @PostMapping("/package")
        public ResponseEntity<PackageResponse> postPackage(
                        @Valid @RequestBody PackageRequest r) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(packageService.createPackage(r));
        }

        @PutMapping("/package/{id}/notify")
        public ResponseEntity<PackageResponse> notifyPackage(
                        @PathVariable("id") Integer id) {
                return ResponseEntity.ok(packageService.notifyById(id));
        }

        @PutMapping("/package/{id}/pickup")
        public ResponseEntity<PackageResponse> pickupPackage(
                        @PathVariable("id") Integer id,
                        @Valid @RequestBody String pickupAt) {
                return ResponseEntity.ok(packageService.pickupById(id, pickupAt));
        }

        @PutMapping("/repair/{id}")
        public ResponseEntity<RepairResponse> updateRepairById(
                        @PathVariable("id") Integer id,
                        @Valid @RequestBody RepairUpdateRequest u) {
                return ResponseEntity.ok(repairRequestService.updateById(id, u));
        }

        @PutMapping("/repair/{id}/complete")
        public ResponseEntity<RepairResponse> completeRepairById(
                        @PathVariable("id") Integer id,
                        Authentication authentication) {
                String name = authentication.getName();
                return ResponseEntity.ok(repairRequestService.completeById(id, name));
        }

        @DeleteMapping("/repair/{id}")
        public ResponseEntity<Void> deleteRepairById(
                        @PathVariable("id") Integer id) {
                repairRequestService.deleteById(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/get-all-residents-users")
        public ResponseEntity<List<UserResponse>> getAllResidentUsers() {
            return ResponseEntity.ok(userService.getAllResidentUsers());
        }
        
}
