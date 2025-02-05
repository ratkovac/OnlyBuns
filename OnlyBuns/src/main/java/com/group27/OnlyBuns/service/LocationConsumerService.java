package com.group27.OnlyBuns.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group27.OnlyBuns.model.Location;
import com.group27.OnlyBuns.repository.LocationRepository;
import dto.LocationDto;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class LocationConsumerService {

    private final LocationRepository locationRepository;

    public LocationConsumerService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @JmsListener(destination = "location-queue")
    public void receiveMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LocationDto locationDto = mapper.readValue(message, LocationDto.class);
            System.out.println("Received: " + locationDto.getLatitude() + ", " + locationDto.getLongitude());
            locationRepository.save(new Location(locationDto.getLatitude(), locationDto.getLongitude()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
