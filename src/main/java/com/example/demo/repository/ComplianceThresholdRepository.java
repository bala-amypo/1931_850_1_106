package com.example.demo.repository;

import com.example.demo.entity.ComplianceThreshold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComplianceThresholdRepository extends JpaRepository<ComplianceThreshold, Long> {
    Optional<ComplianceThreshold> findBySensorType(String sensorType);
}
