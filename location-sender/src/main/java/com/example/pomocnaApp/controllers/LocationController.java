package com.example.pomocnaApp.controllers;

import com.example.pomocnaApp.dtos.LocationDto;
import com.example.pomocnaApp.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendLocation(@RequestBody LocationDto locationDto) {
        try {
            locationService.sendLocation(locationDto);
            return ResponseEntity.ok("Location successfully sent to ActiveMQ queue.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send location: " + e.getMessage());
        }
    }
}
