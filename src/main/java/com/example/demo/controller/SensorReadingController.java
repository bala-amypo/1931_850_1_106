package com.example.demo.controller;

import com.example.demo.entity.Sensor;
import com.example.demo.entity.SensorReading;
import com.example.demo.repository.SensorReadingRepository;
import com.example.demo.repository.SensorRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/readings")
@Tag(name = "Sensor Readings", description = "Sensor reading submission and retrieval endpoints")
public class SensorReadingController {

    private final SensorReadingRepository readingRepository;
    private final SensorRepository sensorRepository;

    public SensorReadingController(SensorReadingRepository readingRepository,
                                   SensorRepository sensorRepository) {
        this.readingRepository = readingRepository;
        this.sensorRepository = sensorRepository;
    }

    @PostMapping("/{sensorId}")
    public SensorReading submitReading(@PathVariable Long sensorId,
                                       @RequestBody SensorReading reading) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
        reading.setSensor(sensor);
        reading.setReadingTime(LocalDateTime.now());
        reading.setStatus("PENDING");
        return readingRepository.save(reading);
    }

    @GetMapping("/{id}")
    public SensorReading getReading(@PathVariable Long id) {
        return readingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reading not found"));
    }

    @GetMapping("/sensor/{sensorId}")
    public List<SensorReading> getReadingsBySensor(@PathVariable Long sensorId) {
        return readingRepository.findBySensor_Id(sensorId);
    }
}
