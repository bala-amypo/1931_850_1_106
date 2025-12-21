package com.example.demo.service.impl;

import com.example.demo.entity.Sensor;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SensorRepository;
import com.example.demo.service.SensorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;

    public SensorServiceImpl(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public Sensor createSensor(Sensor sensor) {
        // Validate sensorType
        if (sensor.getSensorType() == null || sensor.getSensorType().isEmpty()) {
            throw new IllegalArgumentException("sensorType is required");
        }

        // Set default isActive if not provided
        if (sensor.getIsActive() == null) {
            sensor.setIsActive(true);
        }

        return sensorRepository.save(sensor);
    }

    @Override
    public Sensor getSensor(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found"));
    }

    @Override
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }
}
