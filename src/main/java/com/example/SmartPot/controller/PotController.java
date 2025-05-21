package com.example.SmartPot.controller;

import com.example.SmartPot.config.JwtUtil;
import com.example.SmartPot.dto.PotCreationDTO;
import com.example.SmartPot.dto.PotResponseDTO;
import com.example.SmartPot.exceptions.ResourceNotFoundException;
import com.example.SmartPot.model.Pot;
import com.example.SmartPot.service.PotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pots")
public class PotController {

    @Autowired
    private PotService potService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getAllPots() {
        String token = extractTokenFromHeader();
        Long userId = Long.valueOf(jwtUtil.extractUserId(token));
        String role = jwtUtil.extractRole(token);
        List<PotResponseDTO> pots;
        if (role.equals("ROLE_ADMIN")) {
            pots = potService.getAllPots();
        } else {
            pots = potService.getAllPotsByUser(userId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(pots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPotById(@PathVariable Long id) {
        try {
            String token = extractTokenFromHeader();
            Long userId = Long.valueOf(jwtUtil.extractUserId(token));
            String role = jwtUtil.extractRole(token);
            Pot pot = potService.getPotById(id, userId, role);
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
            String token = extractTokenFromHeader();
            Long userId = Long.valueOf(jwtUtil.extractUserId(token));
            requestDTO.setUserId(userId); // Ensure the pot is created for the authenticated user
            Pot pot = potService.createPot(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(pot);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePot(@PathVariable Long id, @RequestBody Pot pot) {
        try {
            String token = extractTokenFromHeader();
            Long userId = Long.valueOf(jwtUtil.extractUserId(token));
            String role = jwtUtil.extractRole(token);
            Pot updatedPot = potService.updatePot(id, pot, userId, role);
            return ResponseEntity.ok(updatedPot);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePot(@PathVariable Long id) {
        try {
            String token = extractTokenFromHeader();
            Long userId = Long.valueOf(jwtUtil.extractUserId(token));
            String role = jwtUtil.extractRole(token);
            potService.deletePot(id, userId, role);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllPotsByUserId(@PathVariable Long id) {
        String token = extractTokenFromHeader();
        Long authenticatedUserId = Long.valueOf(jwtUtil.extractUserId(token));
        String role = jwtUtil.extractRole(token);
        if (!role.equals("ROLE_ADMIN") && !authenticatedUserId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only access your own pots.");
        }
        List<PotResponseDTO> pots = potService.getAllPotsByUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(pots);
    }

    private String extractTokenFromHeader() {
        String authHeader = SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("JWT Token not found in request headers");
    }
}