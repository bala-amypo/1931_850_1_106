package com.example.demo.repository;

import com.example.demo.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findBySensor_Id(Long sensorId);

    List<SensorReading> findBySensor_IdAndReadingTimeBetween(Long sensorId, LocalDateTime start, LocalDateTime end);
}
