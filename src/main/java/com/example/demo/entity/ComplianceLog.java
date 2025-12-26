package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class ComplianceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private SensorReading sensorReading;

    private String statusAssigned;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SensorReading getSensorReading() { return sensorReading; }
    public void setSensorReading(SensorReading sensorReading) { this.sensorReading = sensorReading; }

    public String getStatusAssigned() { return statusAssigned; }
    public void setStatusAssigned(String statusAssigned) { this.statusAssigned = statusAssigned; }
}
