package com.example.demo2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo2.entity.Reservation;

public interface ReservationDao extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByFacility_FacilityId(Integer facilityId);

    List<Reservation> findByUser_UserId(Integer userId);

    List<Reservation> findByFacility_FacilityIdAndUser_UserId(Integer facilityId, Integer userId);

    @Query("SELECT COALESCE(SUM(r.attendees), 0) FROM Reservation r " +
            "WHERE r.facility.facilityId = :facilityId " +
            "AND r.date = :date " +
            "AND r.startTime = :startTime " +
            "AND r.status = org.example.model.ReservationStatus.CONFIRMED")
    Integer sumAttendeesByFacilityIdAndDateAndStartTime(
        @Param("facilityId") Integer facilityId, 
        @Param("date") String date,
        @Param("startTime") String startTime
    );
}
