package com.example.demo2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo2.dto.request.FacilityRequest;
import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.entity.Facility;
import com.example.demo2.repository.FacilityDao;
import com.example.demo2.repository.ReservationDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityDao facilityRepository;
    private final ReservationDao reservationRepository;

    @Transactional
    public FacilityResponse registFacility(FacilityRequest request) {
        Facility facility = new Facility(
                request.name(),
                request.description(),
                request.capacity(),
                request.openTime(),
                request.closeTime(),
                request.isReservable(),
                request.isAvailable());
        facilityRepository.save(facility);
        return FacilityResponse.from(facility);
    }

    @Transactional
    public FacilityResponse updateFacility(Integer id, FacilityRequest request) {
        Facility facility = facilityRepository.findById(id != null ? id : 0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到設施"));
        facility.setName(request.name());
        facility.setDescription(request.description());
        facility.setCapacity(request.capacity());
        facility.setOpenTime(request.openTime());
        facility.setCloseTime(request.closeTime());
        facility.setReservable(request.isReservable());
        facility.setAvailable(request.isAvailable());
        return FacilityResponse.from(facility);
    }

    @Transactional
    public void deleteFacility(Integer id) {
        reservationRepository.deleteByFacility_FacilityId(id);
        facilityRepository.deleteById(id != null ? id : 0);
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
}
