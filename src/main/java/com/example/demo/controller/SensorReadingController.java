// package com.example.demo.controller;

// import com.example.demo.entity.SensorReading;
// import com.example.demo.service.SensorReadingService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/readings")
// @Tag(name = "Sensor Readings", description = "Sensor reading management endpoints")
// public class SensorReadingController {

//     private final SensorReadingService readingService;

//     public SensorReadingController(SensorReadingService readingService) {
//         this.readingService = readingService;
//     }

//     @PostMapping("/{sensorId}")
//     @Operation(summary = "Submit a new reading", description = "Submit a new sensor reading for a specific sensor")
//     public ResponseEntity<SensorReading> submitReading(
//             @Parameter(description = "Sensor ID") @PathVariable Long sensorId,
//             @RequestBody SensorReading reading) {
//         SensorReading savedReading = readingService.submitReading(sensorId, reading);
//         return ResponseEntity.ok(savedReading);
//     }

//     @GetMapping("/sensor/{sensorId}")
//     @Operation(summary = "Get readings by sensor", description = "Retrieve all readings for a specific sensor")
//     public ResponseEntity<List<SensorReading>> getReadingsBySensor(
//             @Parameter(description = "Sensor ID") @PathVariable Long sensorId) {
//         List<SensorReading> readings = readingService.getReadingsBySensor(sensorId);
//         return ResponseEntity.ok(readings);
//     }

//     @GetMapping("/{id}")
//     @Operation(summary = "Get reading by ID", description = "Retrieve a specific reading by its ID")
//     public ResponseEntity<SensorReading> getReading(
//             @Parameter(description = "Reading ID") @PathVariable Long id) {
//         SensorReading reading = readingService.getReading(id);
//         return ResponseEntity.ok(reading);
//     }
// }
