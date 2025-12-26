package com.example.demo.controller;

import com.example.demo.entity.Location;
import com.example.demo.entity.Sensor;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.SensorRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@Tag(name = "Sensors", description = "Sensor management endpoints")
public class SensorController {

    private final SensorRepository sensorRepository;
    private final LocationRepository locationRepository;

    public SensorController(SensorRepository sensorRepository,
                            LocationRepository locationRepository) {
        this.sensorRepository = sensorRepository;
        this.locationRepository = locationRepository;
    }

    @PostMapping("/{locationId}")
    public Sensor createSensor(@PathVariable Long locationId,
                               @RequestBody Sensor sensor) {
        Location loc = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        sensor.setLocation(loc);

        // set installation timestamp
        sensor.setInstalledAt(java.time.LocalDateTime.now());

        return sensorRepository.save(sensor);
    }

    @GetMapping
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Sensor getSensor(@PathVariable Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
    }
}
