package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.Location;
import com.group27.OnlyBuns.repository.*;
import dto.LocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // Kreiranje novog korisnika
    public Location createLocation(LocationDto locationDto) {
        Location location = new Location(locationDto.getLatitude(), locationDto.getLongitude());
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
