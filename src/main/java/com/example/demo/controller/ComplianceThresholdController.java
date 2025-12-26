// package com.example.demo.controller;

// import com.example.demo.entity.ComplianceThreshold;
// import com.example.demo.service.ComplianceThresholdService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/thresholds")
// @Tag(name = "Compliance Thresholds", description = "Compliance threshold management endpoints")
// public class ComplianceThresholdController {
    
//     private final ComplianceThresholdService thresholdService;
    
//     public ComplianceThresholdController(ComplianceThresholdService thresholdService) {
//         this.thresholdService = thresholdService;
//     }
    
//     @PostMapping
//     @Operation(summary = "Create a new threshold", description = "Add a new compliance threshold for a sensor type")
//     public ResponseEntity<ComplianceThreshold> createThreshold(@RequestBody ComplianceThreshold threshold) {
//         ComplianceThreshold savedThreshold = thresholdService.createThreshold(threshold);
//         return ResponseEntity.ok(savedThreshold);
//     }
    
//     @GetMapping
//     @Operation(summary = "Get all thresholds", description = "Retrieve a list of all compliance thresholds")
//     public ResponseEntity<List<ComplianceThreshold>> getAllThresholds() {
//         List<ComplianceThreshold> thresholds = thresholdService.getAllThresholds();
//         return ResponseEntity.ok(thresholds);
//     }
    
//     @GetMapping("/{id}")
//     @Operation(summary = "Get threshold by ID", description = "Retrieve a specific threshold by its ID")
//     public ResponseEntity<ComplianceThreshold> getThreshold(
//             @Parameter(description = "Threshold ID") @PathVariable Long id) {
//         ComplianceThreshold threshold = thresholdService.getThreshold(id);
//         return ResponseEntity.ok(threshold);
//     }
    
//     @GetMapping("/type/{sensorType}")
//     @Operation(summary = "Get threshold by sensor type", description = "Retrieve threshold for a specific sensor type")
//     public ResponseEntity<ComplianceThreshold> getThresholdBySensorType(
//             @Parameter(description = "Sensor type") @PathVariable String sensorType) {
//         ComplianceThreshold threshold = thresholdService.getThresholdBySensorType(sensorType);
//         return ResponseEntity.ok(threshold);
//     }
// }