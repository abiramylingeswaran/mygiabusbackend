package com.mygia.bus.service;

import com.mygia.bus.domain.Location;
import com.mygia.bus.dto.location.LocationRequest;
import com.mygia.bus.dto.location.LocationResponse;
import com.mygia.bus.exception.ApiException;
import com.mygia.bus.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationResponse> search(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return locationRepository.searchByNamePrefix(query.trim()).stream().map(this::toDto).toList();
    }

    public List<LocationResponse> findAll() {
        return locationRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public LocationResponse create(LocationRequest request) {
        if (locationRepository.existsByNameIgnoreCase(request.name())) {
            throw new ApiException("Location already exists");
        }
        Location saved = locationRepository.save(Location.builder()
                .name(request.name().trim())
                .district(request.district().trim())
                .build());
        return toDto(saved);
    }

    @Transactional
    public LocationResponse update(Long id, LocationRequest request) {
        Location loc = locationRepository.findById(id).orElseThrow(() -> new ApiException("Location not found"));
        loc.setName(request.name().trim());
        loc.setDistrict(request.district().trim());
        return toDto(loc);
    }

    @Transactional
    public void delete(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ApiException("Location not found");
        }
        locationRepository.deleteById(id);
    }

    private LocationResponse toDto(Location loc) {
        return new LocationResponse(loc.getId(), loc.getName(), loc.getDistrict());
    }
}
