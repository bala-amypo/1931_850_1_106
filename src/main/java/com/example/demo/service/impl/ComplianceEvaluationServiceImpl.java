package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ComplianceEvaluationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplianceEvaluationServiceImpl implements ComplianceEvaluationService {

    private final SensorReadingRepository readingRepository;
    private final ComplianceThresholdRepository thresholdRepository;
    private final ComplianceLogRepository logRepository;

    public ComplianceEvaluationServiceImpl(SensorReadingRepository readingRepository,
                                           ComplianceThresholdRepository thresholdRepository,
                                           ComplianceLogRepository logRepository) {
        this.readingRepository = readingRepository;
        this.thresholdRepository = thresholdRepository;
        this.logRepository = logRepository;
    }

    @Override
    public ComplianceLog evaluateReading(Long readingId) {
        SensorReading reading = readingRepository.findById(readingId)
                .orElseThrow(() -> new RuntimeException("Reading not found"));

        List<ComplianceLog> existing = logRepository.findBySensorReading_Id(readingId);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }

        ComplianceThreshold threshold = thresholdRepository
                .findBySensorType(reading.getSensor().getSensorType())
                .orElseThrow(() -> new RuntimeException("Threshold not found"));

        String status = (reading.getReadingValue() >= threshold.getMinValue()
                && reading.getReadingValue() <= threshold.getMaxValue())
                ? "SAFE" : "UNSAFE";

        reading.setStatus(status);
        readingRepository.save(reading);

        ComplianceLog log = new ComplianceLog();
        log.setSensorReading(reading);
        log.setStatusAssigned(status);
        return logRepository.save(log);
    }
}
