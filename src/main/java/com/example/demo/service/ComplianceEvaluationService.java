package com.example.demo.service;

import com.example.demo.entity.SensorReading;

public interface SensorReadingService {
    SensorReading submitReading(Long sensorId, SensorReading reading);
    SensorReading getReading(Long id);
}
