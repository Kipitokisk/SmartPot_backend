package com.example.SmartPot.controller;

import com.example.SmartPot.model.WateringEvent;
import com.example.SmartPot.repository.WateringEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watering-events")
public class WateringEventController {

    @Autowired
    private WateringEventRepository wateringEventRepository;

    @GetMapping
    public List<WateringEvent> getAllWateringEvents() {
        return wateringEventRepository.findAll();
    }

    @GetMapping("/{id}")
    public WateringEvent getWateringEventById(@PathVariable Long id) {
        return wateringEventRepository.findById(id).orElseThrow(() -> new RuntimeException("Watering event not found"));
    }

    @GetMapping("/pot/{potId}")
    public List<WateringEvent> getWateringEventsByPotId(@PathVariable Long potId) {
        return wateringEventRepository.findByPotId(potId);
    }

    @PostMapping
    public WateringEvent createWateringEvent(@RequestBody WateringEvent wateringEvent) {
        return wateringEventRepository.save(wateringEvent);
    }

    @PutMapping("/{id}")
    public WateringEvent updateWateringEvent(@PathVariable Long id, @RequestBody WateringEvent wateringEvent) {
        WateringEvent existingEvent = wateringEventRepository.findById(id).orElseThrow(() -> new RuntimeException("Watering event not found"));
        existingEvent.setTimestamp(wateringEvent.getTimestamp());
        existingEvent.setDurationSeconds(wateringEvent.getDurationSeconds());
        existingEvent.setMethod(wateringEvent.getMethod());
        return wateringEventRepository.save(existingEvent);
    }

    @DeleteMapping("/{id}")
    public void deleteWateringEvent(@PathVariable Long id) {
        wateringEventRepository.deleteById(id);
    }
}
