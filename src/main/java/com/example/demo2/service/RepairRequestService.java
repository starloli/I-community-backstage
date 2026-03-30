package com.example.demo2.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.RepairRequestDto;
import com.example.demo2.dto.request.RepairUpdateRequest;
import com.example.demo2.dto.response.RepairResponse;
import com.example.demo2.entity.RepairRequest;
import com.example.demo2.entity.User;
import com.example.demo2.enums.RepairStatus;
import com.example.demo2.exception.NotFoundException;
import com.example.demo2.repository.RepairRequestDao;
import com.example.demo2.repository.UserDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RepairRequestService {
    
    private final RepairRequestDao repairRequestDao;
    private final UserDao userRepository;

    @Transactional
    public RepairResponse createRepairRequest(String name, RepairRequestDto d) {
        User user = getUser(name);
        RepairRequest request = new RepairRequest(
            user,
            d.location(),
            d.category(),
            d.description(),
            d.imageUrl(),
            d.submittedAt()
        );
        repairRequestDao.save(request);
        return RepairResponse.from(request);
    }

    @Transactional(readOnly = true)
    public List<RepairResponse> searchAll() {
        return repairRequestDao.findAll().stream().map(RepairResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<RepairResponse> searchUserAll(String name) {
        User user = getUser(name);
        return repairRequestDao.findByUser(user).stream().map(RepairResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public long searchPendingNum() {
        return repairRequestDao.countByStatus(RepairStatus.PENDING);
    }

    @Transactional
    public RepairResponse updateById(Integer id, RepairUpdateRequest u) {
        RepairRequest request = repairRequestDao.findById(id)
                .orElseThrow(() -> new NotFoundException("找不到報修單"));
        request.setLocation(u.location());
        request.setDescription(u.description());
        request.setCategory(u.category());
        request.setImageUrl(u.imageUrl());
        request.setStatus(u.status());
        return RepairResponse.from(request);
    }

    @Transactional
    public RepairResponse completeById(Integer id, String name) {
        User user = getUser(name);
        RepairRequest request = repairRequestDao.findById(id)
                .orElseThrow(() -> new NotFoundException("找不到報修單"));
        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy/M/d")
                        .withLocale(Locale.TAIWAN);
        String result = LocalDate.now().format(formatter);
        request.setResolvedAt(result);
        request.setStatus(RepairStatus.DONE);
        request.setHandler(user);
        return RepairResponse.from(request);
    }

    @Transactional
    public void deleteById(Integer id) {
        repairRequestDao.deleteById(id);
    }

    private User getUser(String name) {
        User user =  userRepository.findByUserName(name)
            .orElseThrow(() -> new NotFoundException("user not exists"));
        return user;
    }
}
