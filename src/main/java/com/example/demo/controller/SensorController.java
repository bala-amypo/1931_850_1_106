package com.example.demo.controller;

import com.example.demo.entity.Sensor;
import com.example.demo.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@Tag(name = "Sensors", description = "Sensor management endpoints")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping("/{locationId}")
    @Operation(summary = "Create a new sensor", description = "Add a new sensor to a specific location")
    public ResponseEntity<Sensor> createSensor(
            @Parameter(description = "Location ID") @PathVariable Long locationId,
            @RequestBody Sensor sensor) {
        Sensor savedSensor = sensorService.createSensor(locationId, sensor);
        return ResponseEntity.ok(savedSensor);
    }

    @GetMapping
    @Operation(summary = "Get all sensors", description = "Retrieve a list of all sensors")
    public ResponseEntity<List<Sensor>> getAllSensors() {
        List<Sensor> sensors = sensorService.getAllSensors();
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sensor by ID", description = "Retrieve a specific sensor by its ID")
    public ResponseEntity<Sensor> getSensor(
            @Parameter(description = "Sensor ID") @PathVariable Long id) {
        Sensor sensor = sensorService.getSensor(id);
        return ResponseEntity.ok(sensor);
    }
}
