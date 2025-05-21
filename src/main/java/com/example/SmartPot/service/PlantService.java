package com.example.SmartPot.service;

import com.example.SmartPot.exceptions.PlantCreationException;
import com.example.SmartPot.exceptions.ResourceNotFoundException;
import com.example.SmartPot.model.Plant;
import com.example.SmartPot.repository.PlantRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PlantService {
    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public Plant getPlantById(Long id) {
        return plantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plant with id " + id + " not found."));
    }

    public Plant createPlant(Plant plant) {
        try {
            return plantRepository.save(plant);
        } catch (DataAccessException e) {
            throw new PlantCreationException("Could not save plant with id: " + plant.getId());
        }
    }

}
