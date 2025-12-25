package com.example.demo.service.impl;

import com.example.demo.entity.Sensor;
import com.example.demo.entity.SensorReading;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SensorReadingRepository;
import com.example.demo.repository.SensorRepository;
import com.example.demo.service.SensorReadingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorReadingServiceImpl implements SensorReadingService {

    private final SensorReadingRepository readingRepository;
    private final SensorRepository sensorRepository;

    public SensorReadingServiceImpl(SensorReadingRepository readingRepository, SensorRepository sensorRepository) {
        this.readingRepository = readingRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public SensorReading submitReading(Long sensorId, SensorReading reading) {
        if (reading.getReadingValue() == null || reading.getReadingValue() == 0) {
            throw new IllegalArgumentException("readingValue required");
        }

        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found with id: " + sensorId));

        // Validate readingTime is not in future
        if (reading.getReadingTime() != null && reading.getReadingTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("readingTime cannot be in the future");
        }

        reading.setSensor(sensor);
        reading.setStatus("PENDING");
        if (reading.getReadingTime() == null) {
            reading.setReadingTime(LocalDateTime.now());
        }

        return readingRepository.save(reading);
    }

    @Override
    public SensorReading getReading(Long id) {
        return readingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reading not found with id: " + id));
    }

    @Override
    public List<SensorReading> getReadingsBySensor(Long sensorId) {
        return readingRepository.findBySensor_Id(sensorId);
    }
}
