package com.example.demo2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.Facility;
import com.example.demo2.entity.Reservation;
import com.example.demo2.entity.User;

public interface ReservationDao extends JpaRepository<Reservation, Integer>{
    Optional<Reservation> findByFacility(Facility facility);
    Optional<Reservation> findByUser(User user);
    Optional<Reservation> findByFacilityAndUser(Facility facility, User user);
}
