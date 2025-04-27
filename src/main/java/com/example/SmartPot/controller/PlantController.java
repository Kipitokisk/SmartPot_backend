package com.example.SmartPot.controller;

import com.example.SmartPot.model.Plant;
import com.example.SmartPot.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;

    @GetMapping
    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    @GetMapping("/{id}")
    public Plant getPlantById(@PathVariable Long id) {
        return plantRepository.findById(id).orElseThrow(() -> new RuntimeException("Plant not found"));
    }

    @PostMapping
    public Plant createPlant(@RequestBody Plant plant) {
        return plantRepository.save(plant);
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
