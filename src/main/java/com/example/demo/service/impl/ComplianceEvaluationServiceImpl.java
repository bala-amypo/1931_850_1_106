package com.example.demo.service.impl;

import com.example.demo.entity.ComplianceLog;
import com.example.demo.entity.ComplianceThreshold;
import com.example.demo.entity.SensorReading;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ComplianceLogRepository;
import com.example.demo.repository.ComplianceThresholdRepository;
import com.example.demo.repository.SensorReadingRepository;
import com.example.demo.service.ComplianceEvaluationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplianceEvaluationServiceImpl implements ComplianceEvaluationService {

    private final SensorReadingRepository readingRepository;
    private final ComplianceThresholdRepository thresholdRepository;
    private final ComplianceLogRepository logRepository;

    public ComplianceEvaluationServiceImpl(
            SensorReadingRepository readingRepository,
            ComplianceThresholdRepository thresholdRepository,
            ComplianceLogRepository logRepository) {
        this.readingRepository = readingRepository;
        this.thresholdRepository = thresholdRepository;
        this.logRepository = logRepository;
    }

    @Override
    @Transactional
    public ComplianceLog evaluateReading(Long readingId) {
        // Get the reading
        SensorReading reading = readingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException("Reading not found with id: " + readingId));

        // Get the threshold for this sensor type
        String sensorType = reading.getSensor().getSensorType();
        ComplianceThreshold threshold = thresholdRepository.findBySensorType(sensorType)
                .orElseThrow(() -> new ResourceNotFoundException("Threshold not found for sensor type: " + sensorType));

        // Check if log already exists for this reading
        List<ComplianceLog> existingLogs = logRepository.findBySensorReading_Id(readingId);

        // Evaluate compliance
        Double value = reading.getReadingValue();
        String status;
        String remarks;

        if (value >= threshold.getMinValue() && value <= threshold.getMaxValue()) {
            status = "SAFE";
            remarks = "Reading within acceptable range";
        } else {
            status = "UNSAFE";
            if (value < threshold.getMinValue()) {
                remarks = "Reading below minimum threshold";
            } else {
                remarks = "Reading above maximum threshold";
            }
        }

        ComplianceLog log;
        if (!existingLogs.isEmpty()) {
            // Update existing log
            log = existingLogs.get(0);
            log.setStatusAssigned(status);
            log.setRemarks(remarks);
            log.setThresholdUsed(threshold);
            log.setLoggedAt(LocalDateTime.now());
        } else {
            // Create new log
            log = new ComplianceLog();
            log.setSensorReading(reading);
            log.setThresholdUsed(threshold);
            log.setStatusAssigned(status);
            log.setRemarks(remarks);
            log.setLoggedAt(LocalDateTime.now());
        }

        // Update reading status
        reading.setStatus(status);
        readingRepository.save(reading);

        return logRepository.save(log);
    }

    @Override
    public List<ComplianceLog> getLogsByReading(Long readingId) {
        return logRepository.findBySensorReading_Id(readingId);
    }

    @Override
    public ComplianceLog getLog(Long id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log not found with id: " + id));
    }
}
