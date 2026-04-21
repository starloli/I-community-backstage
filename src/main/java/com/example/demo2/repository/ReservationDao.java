package com.example.demo2.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo2.entity.Reservation;
import com.example.demo2.enums.ReservationStatus;

public interface ReservationDao extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByFacility_FacilityId(Integer facilityId);

    List<Reservation> findByUser_UserId(Integer userId);

    List<Reservation> findByFacility_FacilityIdAndUser_UserId(Integer facilityId, Integer userId);

    void deleteByFacility_FacilityId(Integer facilityId);

    // 根據時段，取得設備預約人數，不計算已取消的預約
    @Query("SELECT COALESCE(SUM(r.attendees), 0) FROM Reservation r " +
            "WHERE r.facility.facilityId = :facilityId " +
            "AND r.date = :date " +
            "AND r.startTime = :startTime " +
            "AND r.status = :status")
    Integer sumAttendeesByFacilityIdAndDateAndStartTimeAndStatus(
            @Param("facilityId") Integer facilityId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("status") ReservationStatus status);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.status = :status " +
            "AND r.user.userId = :userId")
    List<Reservation> findByUser_UserIdAndStatus(
            @Param("userId") Integer userId,
            @Param("status") ReservationStatus status);

    @Query("SELECT DISTINCT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.facility " +
            "WHERE r.status = :status " +
            "AND r.date = :date " +
            "ORDER BY r.startTime DESC, r.reservationId ASC")
    List<Reservation> findAllWithUserByDateAndStatus(
            @Param("status") ReservationStatus status,
            @Param("date") LocalDate date);
}
