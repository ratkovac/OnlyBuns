package com.example.pomocnaApp.services;

import com.example.pomocnaApp.dtos.LocationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public LocationService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendLocation(LocationDto locationDto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(locationDto);
            jmsTemplate.convertAndSend("location-queue", jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
