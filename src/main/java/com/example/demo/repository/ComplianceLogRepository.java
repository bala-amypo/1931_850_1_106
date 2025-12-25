package com.example.demo.repository;

import com.example.demo.entity.ComplianceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplianceLogRepository extends JpaRepository<ComplianceLog, Long> {
    List<ComplianceLog> findBySensorReading_Id(Long readingId);
}
