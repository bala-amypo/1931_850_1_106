// package com.example.demo.controller;

// import com.example.demo.entity.ComplianceLog;
// import com.example.demo.service.ComplianceEvaluationService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/compliance")
// @Tag(name = "Compliance Evaluation", description = "Compliance evaluation and logging endpoints")
// public class ComplianceEvaluationController {
    
//     private final ComplianceEvaluationService evaluationService;
    
//     public ComplianceEvaluationController(ComplianceEvaluationService evaluationService) {
//         this.evaluationService = evaluationService;
//     }
    
//     @PostMapping("/evaluate/{readingId}")
//     @Operation(summary = "Evaluate a reading", description = "Evaluate a sensor reading against compliance thresholds")
//     public ResponseEntity<ComplianceLog> evaluateReading(
//             @Parameter(description = "Reading ID") @PathVariable Long readingId) {
//         ComplianceLog log = evaluationService.evaluateReading(readingId);
//         return ResponseEntity.ok(log);
//     }
    
//     @GetMapping("/reading/{readingId}")
//     @Operation(summary = "Get logs by reading", description = "Retrieve all compliance logs for a specific reading")
//     public ResponseEntity<List<ComplianceLog>> getLogsByReading(
//             @Parameter(description = "Reading ID") @PathVariable Long readingId) {
//         List<ComplianceLog> logs = evaluationService.getLogsByReading(readingId);
//         return ResponseEntity.ok(logs);
//     }
    
//     @GetMapping("/{id}")
//     @Operation(summary = "Get log by ID", description = "Retrieve a specific compliance log by its ID")
//     public ResponseEntity<ComplianceLog> getLog(
//             @Parameter(description = "Log ID") @PathVariable Long id) {
//         ComplianceLog log = evaluationService.getLog(id);
//         return ResponseEntity.ok(log);
//     }
// }