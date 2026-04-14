package com.example.demo2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.PackageRequest;
import com.example.demo2.dto.response.PackageResponse;
import com.example.demo2.repository.PackageDao;
import com.example.demo2.repository.UserDao;
import com.example.demo2.entity.Package;
import com.example.demo2.entity.User;
import com.example.demo2.enums.PackageStatus;
import com.example.demo2.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageDao packageDao;
    private final UserDao userDao;

    @Transactional
    public PackageResponse createPackage(PackageRequest r) {
        Package p = new Package(
                r.recipientName(),
                r.phoneNumber(),
                r.unitNumber(),
                r.trackingNumber(),
                r.courier(),
                r.arrivedAt(),
                r.notes());
        packageDao.save(p);
        return PackageResponse.from(p);
    }

    @Transactional(readOnly = true)
    public List<PackageResponse> searchAll() {
        return packageDao.findAll().stream()
                .map(PackageResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<PackageResponse> searchByUser(String name) {
        User user = userDao.findByUserName(name)
                .orElseThrow(() -> new NotFoundException("找不到使用者"));
        return packageDao.findByUnitNumber(user.getUnitNumber()).stream()
                .map(PackageResponse::from).toList();
    }

    @Transactional
    public PackageResponse notifyById(Integer id) {
        Package p = packageDao.findById(id != null ? id : 0)
                .orElseThrow(() -> new NotFoundException("找不到包裹"));
        p.setNotified(true);
        return PackageResponse.from(p);
    }

    @Transactional
    public PackageResponse pickupById(Integer id, String pickupAt) {
        Package p = packageDao.findById(id != null ? id : 0)
                .orElseThrow(() -> new NotFoundException("找不到包裹"));
        p.setStatus(PackageStatus.PICKED_UP);
        p.setPickupAt(pickupAt);
        return PackageResponse.from(p);
    }

    @Transactional(readOnly = true)
    public long geWaittingNumber() {
        return packageDao.countByStatus(PackageStatus.WAITING);
    }
}
