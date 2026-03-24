package com.example.demo2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo2.dto.request.FacilityRegistRequest;
import com.example.demo2.dto.response.FacilityResponse;
import com.example.demo2.entity.Facility;
import com.example.demo2.repository.FacilityDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacilityService {
    
    private final FacilityDao facilityRepository;
    
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

    public ArrayList<FacilityResponse> getFacilities() {
        List<Facility> facilities = facilityRepository.findAll();
        ArrayList<FacilityResponse> facilityResponses = new ArrayList<>();
        for (Facility facility : facilities) {
            facilityResponses.add(FacilityResponse.from(facility));
        }
        return facilityResponses;
    }
}
