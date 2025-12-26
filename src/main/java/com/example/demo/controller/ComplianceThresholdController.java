    package com.example.demo.controller;

    import com.example.demo.entity.ComplianceThreshold;
    import com.example.demo.repository.ComplianceThresholdRepository;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/thresholds")
    @Tag(name = "Compliance Thresholds", description = "Compliance threshold management endpoints")
    public class ComplianceThresholdController {

        private final ComplianceThresholdRepository thresholdRepository;

        public ComplianceThresholdController(ComplianceThresholdRepository thresholdRepository) {
            this.thresholdRepository = thresholdRepository;
        }

        @GetMapping
        public List<ComplianceThreshold> getAllThresholds() {
            return thresholdRepository.findAll();
        }

        @PostMapping
        public ComplianceThreshold createThreshold(@RequestBody ComplianceThreshold t) {
            return thresholdRepository.save(t);
        }

        @GetMapping("/{id}")
        public ComplianceThreshold getThreshold(@PathVariable Long id) {
            return thresholdRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Threshold not found"));
        }

        @GetMapping("/type/{sensorType}")
        public ComplianceThreshold getThresholdBySensorType(@PathVariable String sensorType) {
            return thresholdRepository.findBySensorType(sensorType)
                    .orElseThrow(() -> new RuntimeException("Threshold not found"));
        }
    }
