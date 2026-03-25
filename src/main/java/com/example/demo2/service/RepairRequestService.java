package com.example.demo2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.RepairRequestDto;
import com.example.demo2.dto.response.RepairResponse;
import com.example.demo2.entity.RepairRequest;
import com.example.demo2.entity.User;
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
