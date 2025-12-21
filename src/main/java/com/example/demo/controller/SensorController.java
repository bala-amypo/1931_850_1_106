package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
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

    @PostMapping
    @Operation(summary = "Create sensor", description = "Creates a new water quality sensor")
    public ResponseEntity<ApiResponse<Sensor>> createSensor(@RequestBody Sensor sensor) {
        Sensor createdSensor = sensorService.createSensor(sensor);
        ApiResponse<Sensor> response = new ApiResponse<>(true, "Sensor created successfully", createdSensor);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all sensors", description = "Retrieves all sensors")
    public ResponseEntity<ApiResponse<List<Sensor>>> getAllSensors() {
        List<Sensor> sensors = sensorService.getAllSensors();
        ApiResponse<List<Sensor>> response = new ApiResponse<>(true, "Sensors retrieved successfully", sensors);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sensor by ID", description = "Retrieves a specific sensor by its ID")
    public ResponseEntity<ApiResponse<Sensor>> getSensor(
            @Parameter(description = "Sensor ID") @PathVariable Long id) {
        Sensor sensor = sensorService.getSensor(id);
        ApiResponse<Sensor> response = new ApiResponse<>(true, "Sensor retrieved successfully", sensor);
        return ResponseEntity.ok(response);
    }
}
