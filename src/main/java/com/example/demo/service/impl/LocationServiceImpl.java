package com.example.demo.service.impl;

import com.example.demo.entity.Location;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LocationRepository;
import com.example.demo.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location createLocation(Location location) {
        if (location.getRegion() == null || location.getRegion().trim().isEmpty()) {
            throw new IllegalArgumentException("region required");
        }
        if (location.getLocationName() == null || location.getLocationName().trim().isEmpty()) {
            throw new IllegalArgumentException("locationName required");
        }
        return locationRepository.save(location);
    }

    @Override
    public Location getLocation(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
