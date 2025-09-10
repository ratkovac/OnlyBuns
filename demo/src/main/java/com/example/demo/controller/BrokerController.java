package com.example.demo.controller;

import com.example.demo.service.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
public class BrokerController {

    private final QueueService queueService;

    public BrokerController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping("/{queueName}")
    public ResponseEntity<Void> sendMessage(@PathVariable String queueName, @RequestBody String message) {
        try {
            queueService.sendMessage(queueName, message);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{queueName}")
    public ResponseEntity<String> receiveMessage(@PathVariable String queueName) {
        try {
            return queueService.receiveMessage(queueName)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.noContent().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}