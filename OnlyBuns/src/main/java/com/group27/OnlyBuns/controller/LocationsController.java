package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.Location;
import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.service.EmailSenderService;
import com.group27.OnlyBuns.service.LocationService;
import com.group27.OnlyBuns.service.UserService;
import com.group27.OnlyBuns.utils.SimpleRateLimiter;
import dto.LocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locations")
@CrossOrigin(origins = "http://localhost:4200")
public class LocationsController {

    private final LocationService locationService;

    @Autowired
    public LocationsController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Endpoint za kreiranje novog korisnika
    @PostMapping
    public Location createLocation(@RequestBody LocationDto locationDto) {
        return locationService.createLocation(locationDto);
    }

    // Endpoint za dobijanje korisnika po korisničkom imenu
    @GetMapping("/getAll")
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }
}
