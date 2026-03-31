package com.example.demo2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo2.dto.request.ReservationRequest;
import com.example.demo2.dto.request.SearchReservationRequest;
import com.example.demo2.dto.response.ReservationResponse;
import com.example.demo2.entity.Reservation;
import com.example.demo2.enums.ReservationStatus;
import com.example.demo2.repository.FacilityDao;
import com.example.demo2.repository.ReservationDao;
import com.example.demo2.repository.UserDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDao reservationRepository;
    private final UserDao userRepository;
    private final FacilityDao facilityRepository;

    @Transactional
    public ReservationResponse reserveFacility(ReservationRequest request) {
        Reservation reservation = new Reservation(
                this.userRepository.findById(request.userId() != null ? request.userId() : 0).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到用戶")),
                this.facilityRepository.findById(request.facilityId() != null ? request.facilityId() : 0).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到設施")),
                request.date(),
                request.startTime(),
                request.endTime(),
                request.attendees(),
                request.isReservable(),
                request.isAvailable());
        if (reservation.getFacility().isReservable() == false) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該設施不可預約");
        }
        if (reservation.getFacility().isAvailable() == false) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該設施無法使用");
        }
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
        return ReservationResponse.from(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservations(SearchReservationRequest request) {
        List<ReservationResponse> reservationResponses = new ArrayList<>();
        if (request.facility() != null && request.user() != null) {
            List<Reservation> reservations = reservationRepository.findByFacility_FacilityIdAndUser_UserId(
                    request.facility().getFacilityId(), request.user().getUserId());
            if (reservations != null) {
                for (Reservation reservation : reservations) {
                    reservationResponses.add(ReservationResponse.from(reservation));
                }
            }
        } else if (request.facility() != null) {
            List<Reservation> reservations = reservationRepository.findByFacility_FacilityId(
                    request.facility().getFacilityId());
            if (reservations != null) {
                for (Reservation reservation : reservations) {
                    reservationResponses.add(ReservationResponse.from(reservation));
                }
            }
        } else {
            List<Reservation> reservations = reservationRepository.findByUser_UserId(
                    request.user().getUserId());
            if (reservations != null) {
                for (Reservation reservation : reservations) {
                    reservationResponses.add(ReservationResponse.from(reservation));
                }
            }
        }
        return reservationResponses;
    }

    @Transactional
    public void cancelReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId != null ? reservationId : 0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到預約資料"));
        if (ReservationStatus.CANCELLED.equals(reservation.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "無法再次取消");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByUserId(Integer userId) {
        return reservationRepository.findByUser_UserId(userId).stream().map(r -> ReservationResponse.from(r)).toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByFacilityId(Integer facilityId) {
        return reservationRepository.findByFacility_FacilityId(facilityId).stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .map(r -> ReservationResponse.from(r))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByFacilityIdAndUserId(Integer facilityId, Integer userId) {
        return reservationRepository.findByFacility_FacilityIdAndUser_UserId(facilityId, userId).stream()
                .map(r -> ReservationResponse.from(r)).toList();
    }
}
