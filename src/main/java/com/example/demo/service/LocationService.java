package com.example.demo.service;

import com.example.demo.entity.Location;

public interface LocationService {
    Location createLocation(Location location);
    Location getLocation(Long id);
}
