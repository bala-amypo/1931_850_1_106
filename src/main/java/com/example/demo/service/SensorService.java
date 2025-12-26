package com.example.demo.service;

import com.example.demo.entity.Sensor;
import java.util.List;

public interface SensorService {
    Sensor createSensor(Long locationId, Sensor sensor);
    Sensor getSensor(Long id);
    List<Sensor> getAllSensors();
}
