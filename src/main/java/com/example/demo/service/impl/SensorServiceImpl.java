package com.example.demo.service.impl;

import com.example.demo.entity.Location;
import com.example.demo.entity.Sensor;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.SensorRepository;
import com.example.demo.service.SensorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final LocationRepository locationRepository;

    public SensorServiceImpl(SensorRepository sensorRepository,
                             LocationRepository locationRepository) {
        this.sensorRepository = sensorRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public Sensor createSensor(Long locationId, Sensor sensor) {
        if (sensor.getSensorType() == null || sensor.getSensorType().isBlank()) {
            throw new IllegalArgumentException("sensorType required");
        }
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        sensor.setLocation(location);
        return sensorRepository.save(sensor);
    }

    @Override
    public Sensor getSensor(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
    }

    @Override
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }
}
