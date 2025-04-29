package com.example.SmartPot.controller;

import com.example.SmartPot.dto.PotCreationDTO;
import com.example.SmartPot.dto.PotResponseDTO;
import com.example.SmartPot.exceptions.ResourceAlreadyExistsException;
import com.example.SmartPot.exceptions.ResourceNotFoundException;
import com.example.SmartPot.model.Plant;
import com.example.SmartPot.model.Pot;
import com.example.SmartPot.model.User;
import com.example.SmartPot.repository.PlantRepository;
import com.example.SmartPot.repository.PotRepository;
import com.example.SmartPot.repository.UserRepository;
import com.example.SmartPot.service.PotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pots")
public class PotController {

    @Autowired
    private PotRepository potRepository;
    @Autowired
    private PotService potService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlantRepository plantRepository;

    @GetMapping
    public List<Pot> getAllPots() {
        return potRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPotById(@PathVariable Long id) {
        try {
            Pot pot = potService.getPotById(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(pot);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<?> createPot(@RequestBody PotCreationDTO requestDTO) {
        try {
            Pot pot = potService.createPot(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(pot);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Pot updatePot(@PathVariable Long id, @RequestBody Pot pot) {
        Pot existingPot = potRepository.findById(id).orElseThrow(() -> new RuntimeException("Pot not found"));
        existingPot.setCurrentMoisture(pot.getCurrentMoisture());
        existingPot.setPlant(pot.getPlant());
        existingPot.setUser(pot.getUser());
        return potRepository.save(existingPot);
    }

    @DeleteMapping("/{id}")
    public void deletePot(@PathVariable Long id) {
        potRepository.deleteById(id);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<?> getAllPotsByUserId(@PathVariable Long id) {
        List<PotResponseDTO> pots = potService.getAllPotsByUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(pots);
    }
}
