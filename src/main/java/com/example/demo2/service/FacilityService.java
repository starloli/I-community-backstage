package com.example.demo2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.FacilityRegistRequest;
import com.example.demo2.dto.request.ReservationRequest;
import com.example.demo2.dto.request.SearchReservationRequest;
import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.dto.response.ReservationResponse;
import com.example.demo2.entity.Facility;
import com.example.demo2.entity.Reservation;
import com.example.demo2.enums.ReservationStatus;
import com.example.demo2.repository.FacilityDao;
import com.example.demo2.repository.ReservationDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityDao facilityRepository;
    private final ReservationDao reservationRepository;

    @Transactional(readOnly = true)
    public FacilityResponse registFacility(FacilityRegistRequest request) {
        Facility facility = new Facility(
                request.name(),
                request.description(),
                request.capacity(),
                request.openTime(),
                request.closeTime(),
                request.isAvailable());
        facilityRepository.save(facility);
        return FacilityResponse.from(facility);
    }

    @Transactional(readOnly = true)
    public ArrayList<FacilityResponse> getFacilities() {
        List<Facility> facilities = facilityRepository.findAll();
        ArrayList<FacilityResponse> facilityResponses = new ArrayList<>();
        for (Facility facility : facilities) {
            facilityResponses.add(FacilityResponse.from(facility));
        }
        return facilityResponses;
    }

    @Transactional(readOnly = true)
    public ReservationResponse reserveFacility(ReservationRequest request) {
        Reservation reservation = new Reservation(
                request.user(),
                request.facility(),
                request.date(),
                request.startTime(),
                request.endTime(),
                request.attendees());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
        return ReservationResponse.from(reservation);
    }

    // @Transactional(readOnly = true)
    // public List<ReservationResponse> getReservations(SearchReservationRequest request) {
    //     List<ReservationResponse> reservationResponses = new ArrayList<>();
    //     if (request.facility() != null && request.user() != null) {
    //         List<Reservation> reservations = reservationRepository.findByFacilityAndUser(request.facility(), request.user());

            
    //     } else if (request.facility() != null) {

    //     } else {

    //     }

    // }
}
