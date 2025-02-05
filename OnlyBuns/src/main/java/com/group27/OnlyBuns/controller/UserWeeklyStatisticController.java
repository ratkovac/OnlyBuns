package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.model.UserWeeklyStatistic;
import com.group27.OnlyBuns.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class UserWeeklyStatisticController {

    private final NotificationService service;

    @Autowired
    public UserWeeklyStatisticController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserWeeklyStatistic>> getAllStatistics() {
        return ResponseEntity.ok(service.getAllStatistics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWeeklyStatistic> getStatisticById(@PathVariable Long id) {
        return service.getStatisticById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserWeeklyStatistic> createStatistic(@RequestBody UserWeeklyStatistic statistic) {
        return ResponseEntity.ok(service.createStatistic(statistic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserWeeklyStatistic> updateStatistic(@PathVariable Long id, @RequestBody UserWeeklyStatistic updatedStatistic) {
        return ResponseEntity.ok(service.updateStatistic(id, updatedStatistic));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserWeeklyStatistic> patchStatistic(@PathVariable Long id, @RequestBody UserWeeklyStatistic patchData) {
        return ResponseEntity.ok(service.patchStatistic(id, patchData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatistic(@PathVariable Long id) {
        service.deleteStatistic(id);
        return ResponseEntity.noContent().build();
    }
}