package com.example.SmartPot.service;

import com.example.SmartPot.dto.PotCreationDTO;
import com.example.SmartPot.dto.PotResponseDTO;
import com.example.SmartPot.exceptions.ResourceNotFoundException;
import com.example.SmartPot.model.Plant;
import com.example.SmartPot.model.Pot;
import com.example.SmartPot.model.User;
import com.example.SmartPot.repository.PlantRepository;
import com.example.SmartPot.repository.PotRepository;
import com.example.SmartPot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PotService {
    private final PotRepository potRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public PotService(PotRepository potRepository, UserRepository userRepository, PlantRepository plantRepository) {
        this.potRepository = potRepository;
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }

    public List<PotResponseDTO> getAllPotsByUser(Long userId) {
        return potRepository.findAllByUser_Id(userId).stream().map(pot -> PotResponseDTO.builder()
                .id(pot.getId())
                .userId(pot.getUser().getId())
                .plantId(pot.getPlant().getId())
                .currentMoisture(pot.getCurrentMoisture())
                .waterReservoir(pot.getWaterReservoir())
                .build())
                .toList();
    }

    public Pot createPot(PotCreationDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Plant plant = plantRepository.findById(requestDTO.getPlantId()).orElseThrow(() -> new ResourceNotFoundException("Plant not found."));
        Pot pot = Pot.builder().user(user).plant(plant).currentMoisture(requestDTO.getCurrentMoisture()).waterReservoir(requestDTO.getWaterReservoir()).build();
        return potRepository.save(pot);
    }

    public Pot getPotById(Long id) {
        return potRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pot with id " + id + " not found."));
    }
}
