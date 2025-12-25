package com.example.demo.service;

import com.example.demo.entity.SensorReading;

import java.util.List;

public interface SensorReadingService {
    SensorReading submitReading(Long sensorId, SensorReading reading);

    SensorReading getReading(Long id);

    List<SensorReading> getReadingsBySensor(Long sensorId);
}
