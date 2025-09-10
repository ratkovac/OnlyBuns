package com.group27.OnlyBuns.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group27.OnlyBuns.model.Location;
import com.group27.OnlyBuns.repository.LocationRepository;
import dto.LocationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocalLocationConsumerService {

    private final LocationRepository locationRepository;
    private final RestTemplate restTemplate;
    private final String brokerUrl = "http://localhost:8085/queue/location-queue";

    public LocalLocationConsumerService(LocationRepository locationRepository, RestTemplate restTemplate) {
        this.locationRepository = locationRepository;
        this.restTemplate = restTemplate;
    }

    // Ova metoda će se izvršavati na svakih 5 sekundi
    @Scheduled(fixedDelay = 5000)
    public void pollForMessages() {
        try {
            // Pitamo broker da li ima poruka
            ResponseEntity<String> response = restTemplate.getForEntity(brokerUrl, String.class);

            // Ako je status 200 OK, znači da smo dobili poruku
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                String message = response.getBody();
                if (message != null && !message.isEmpty()) {
                    processMessage(message);
                }
            }
            // Ako je status 204 No Content, nema poruka, ne radimo ništa.

        } catch (Exception e) {
            // Greška ako broker nije dostupan
            // System.err.println("Could not connect to SimpleBroker: " + e.getMessage());
        }
    }

    private void processMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LocationDto locationDto = mapper.readValue(message, LocationDto.class);
            System.out.println("Received from SimpleBroker: " + locationDto.getLatitude() + ", " + locationDto.getLongitude());
            locationRepository.save(new Location(locationDto.getLatitude(), locationDto.getLongitude()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}