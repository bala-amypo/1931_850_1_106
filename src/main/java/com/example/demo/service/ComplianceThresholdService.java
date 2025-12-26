package com.example.demo.service;

import com.example.demo.entity.ComplianceThreshold;

public interface ComplianceThresholdService {
    ComplianceThreshold createThreshold(ComplianceThreshold t);
    ComplianceThreshold getThresholdBySensorType(String sensorType);
}
