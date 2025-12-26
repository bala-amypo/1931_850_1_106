package com.example.demo.service;

import com.example.demo.entity.ComplianceLog;

public interface ComplianceEvaluationService {
    ComplianceLog evaluateReading(Long readingId);
}
