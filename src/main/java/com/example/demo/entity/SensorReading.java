package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensor_readings")
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(nullable = false)
    private Double readingValue;

    @Column(nullable = false)
    private LocalDateTime readingTime;

    @Column(nullable = false)
    private String status = "PENDING";

    @OneToMany(mappedBy = "sensorReading", cascade = CascadeType.ALL)
    private List<ComplianceLog> complianceLogs = new ArrayList<>();

    public SensorReading() {
    }

    public SensorReading(Sensor sensor, Double readingValue, LocalDateTime readingTime, String status) {
        this.sensor = sensor;
        this.readingValue = readingValue;
        this.readingTime = readingTime;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (readingTime == null) {
            readingTime = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Double getReadingValue() {
        return readingValue;
    }

    public void setReadingValue(Double readingValue) {
        this.readingValue = readingValue;
    }

    public LocalDateTime getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(LocalDateTime readingTime) {
        this.readingTime = readingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ComplianceLog> getComplianceLogs() {
        return complianceLogs;
    }

    public void setComplianceLogs(List<ComplianceLog> complianceLogs) {
        this.complianceLogs = complianceLogs;
    }
}
