package com.example.demo2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.Reservation;

public interface ReservationDao extends JpaRepository<Reservation, Integer>{
    List<Reservation> findByFacility_FacilityId(Integer facilityId);
    List<Reservation> findByUser_UserId(Integer userId);
    List<Reservation> findByFacility_FacilityIdAndUser_UserId(Integer facilityId, Integer userId);
}
