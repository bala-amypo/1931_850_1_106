// package com.example.demo.controller;

// import com.example.demo.entity.Location;
// import com.example.demo.service.LocationService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/locations")
// @Tag(name = "Locations", description = "Location management endpoints")
// public class LocationController {

//     private final LocationService locationService;

//     public LocationController(LocationService locationService) {
//         this.locationService = locationService;
//     }

//     @PostMapping
//     @Operation(summary = "Create a new location", description = "Add a new location to the system")
//     public ResponseEntity<Location> createLocation(@RequestBody Location location) {
//         Location savedLocation = locationService.createLocation(location);
//         return ResponseEntity.ok(savedLocation);
//     }

//     @GetMapping
//     @Operation(summary = "Get all locations", description = "Retrieve a list of all locations")
//     public ResponseEntity<List<Location>> getAllLocations() {
//         List<Location> locations = locationService.getAllLocations();
//         return ResponseEntity.ok(locations);
//     }

//     @GetMapping("/{id}")
//     @Operation(summary = "Get location by ID", description = "Retrieve a specific location by its ID")
//     public ResponseEntity<Location> getLocation(
//             @Parameter(description = "Location ID") @PathVariable Long id) {
//         Location location = locationService.getLocation(id);
//         return ResponseEntity.ok(location);
//     }
// }
