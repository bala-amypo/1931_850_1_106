package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sensorCode;

    @Column(nullable = false)
    private String sensorType;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(nullable = false)
    private LocalDateTime installedAt;

    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
    private List<SensorReading> readings = new ArrayList<>();

    public Sensor() {
    }

    public Sensor(String sensorCode, String sensorType, Location location, LocalDateTime installedAt,
            Boolean isActive) {
        this.sensorCode = sensorCode;
        this.sensorType = sensorType;
        this.location = location;
        this.installedAt = installedAt;
        this.isActive = isActive;
    }

    @PrePersist
    protected void onCreate() {
        if (installedAt == null) {
            installedAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(LocalDateTime installedAt) {
        this.installedAt = installedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<SensorReading> getReadings() {
        return readings;
    }

    public void setReadings(List<SensorReading> readings) {
        this.readings = readings;
    }
}
