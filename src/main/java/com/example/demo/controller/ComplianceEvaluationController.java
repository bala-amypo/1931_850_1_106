package com.example.demo.controller;

import com.example.demo.entity.ComplianceLog;
import com.example.demo.repository.ComplianceLogRepository;
import com.example.demo.service.ComplianceEvaluationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
@Tag(name = "Compliance Evaluation", description = "Compliance evaluation and log management endpoints")
public class ComplianceEvaluationController {

    private final ComplianceEvaluationService evaluationService;
    private final ComplianceLogRepository logRepository;

    public ComplianceEvaluationController(ComplianceEvaluationService evaluationService,
                                          ComplianceLogRepository logRepository) {
        this.evaluationService = evaluationService;
        this.logRepository = logRepository;
    }

    @PostMapping("/evaluate/{readingId}")
    public ComplianceLog evaluateReading(@PathVariable Long readingId) {
        return evaluationService.evaluateReading(readingId);
    }

    @GetMapping("/{id}")
    public ComplianceLog getLog(@PathVariable Long id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found"));
    }

    @GetMapping("/reading/{readingId}")
    public List<ComplianceLog> getLogsByReading(@PathVariable Long readingId) {
        return logRepository.findBySensorReading_Id(readingId);
    }
}
