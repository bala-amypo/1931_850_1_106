package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ComplianceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SensorReading sensorReading;

    @ManyToOne
    private ComplianceThreshold thresholdUsed;

    private String statusAssigned;
    private String remarks;
    private LocalDateTime loggedAt;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SensorReading getSensorReading() { return sensorReading; }
    public void setSensorReading(SensorReading sensorReading) { this.sensorReading = sensorReading; }

    public ComplianceThreshold getThresholdUsed() { return thresholdUsed; }
    public void setThresholdUsed(ComplianceThreshold thresholdUsed) { this.thresholdUsed = thresholdUsed; }

    public String getStatusAssigned() { return statusAssigned; }
    public void setStatusAssigned(String statusAssigned) { this.statusAssigned = statusAssigned; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}
