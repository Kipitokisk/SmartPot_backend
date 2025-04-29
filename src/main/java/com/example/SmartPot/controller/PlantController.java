package com.example.SmartPot.controller;

import com.example.SmartPot.exceptions.ResourceAlreadyExistsException;
import com.example.SmartPot.exceptions.ResourceNotFoundException;
import com.example.SmartPot.model.Plant;
import com.example.SmartPot.repository.PlantRepository;
import com.example.SmartPot.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private PlantService plantService;

    @GetMapping
    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlantById(@PathVariable Long id) {
        try {
            Plant plant = plantService.getPlantById(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(plant);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createPlant(@RequestBody Plant plant) {
        try {
            Plant newPlant = plantService.createPlant(plant);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPlant);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Plant updatePlant(@PathVariable Long id, @RequestBody Plant plant) {
        Plant existingPlant = plantRepository.findById(id).orElseThrow(() -> new RuntimeException("Plant not found"));
        existingPlant.setName(plant.getName());
        existingPlant.setOptimalMoistureMin(plant.getOptimalMoistureMin());
        existingPlant.setOptimalMoistureMax(plant.getOptimalMoistureMax());
        return plantRepository.save(existingPlant);
    }

    @DeleteMapping("/{id}")
    public void deletePlant(@PathVariable Long id) {
        plantRepository.deleteById(id);
    }
}
