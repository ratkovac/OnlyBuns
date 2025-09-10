package com.example.pomocnaApp.services;

import com.example.pomocnaApp.dtos.LocationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationService {

    private final RestTemplate restTemplate;
    private final String brokerUrl = "http://localhost:8085/queue/location-queue"; // URL našeg SimpleBroker-a

    @Autowired
    public LocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendLocation(LocationDto locationDto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(locationDto);

            // Šaljemo HTTP POST zahtev našem brokeru
            restTemplate.postForEntity(brokerUrl, jsonMessage, String.class);
            System.out.println("Sent location to SimpleBroker: " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}