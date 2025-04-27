package com.example.SmartPot.controller;

import com.example.SmartPot.model.Pot;
import com.example.SmartPot.repository.PotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pots")
public class PotController {

    @Autowired
    private PotRepository potRepository;

    @GetMapping
    public List<Pot> getAllPots() {
        return potRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pot getPotById(@PathVariable Long id) {
        return potRepository.findById(id).orElseThrow(() -> new RuntimeException("Pot not found"));
    }

    @PostMapping
    public Pot createPot(@RequestBody Pot pot) {
        return potRepository.save(pot);
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
}
