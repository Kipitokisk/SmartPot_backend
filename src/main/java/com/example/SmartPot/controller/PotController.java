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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<String, Object> response = new HashMap<>();
        response.put("success", "true");
        response.put("message", "Pots retrieved successfully");
        response.put("data", pots);

        return ResponseEntity.status(HttpStatus.OK).body(pots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPotById(@PathVariable Long id) {
        try {
            String token = extractTokenFromHeader();
            Long userId = Long.valueOf(jwtUtil.extractUserId(token));
            String role = jwtUtil.extractRole(token);
            Pot pot = potService.getPotById(id, userId, role);

            Map<String, Object> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "Pots retrieved successfully");
            response.put("data", pot);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = getErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = getErrorResponse("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createPot(@RequestBody PotCreationDTO requestDTO) {
        try {
            String token = extractTokenFromHeader();
            Long userId = Long.valueOf(jwtUtil.extractUserId(token));
            requestDTO.setUserId(userId);
            Pot pot = potService.createPot(requestDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "Pot created successfully");
            response.put("data", pot);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = getErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = getErrorResponse("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePot(@PathVariable Long id, @RequestBody Pot pot) {
        try {
            String token = extractTokenFromHeader();
            Long userId = Long.valueOf(jwtUtil.extractUserId(token));
            String role = jwtUtil.extractRole(token);
            Pot updatedPot = potService.updatePot(id, pot, userId, role);

            Map<String, Object> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "Pot updated successfully");
            response.put("data", updatedPot);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = getErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = getErrorResponse("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePot(@PathVariable Long id) {
        try {
            String token = extractTokenFromHeader();
            Long userId = Long.valueOf(jwtUtil.extractUserId(token));
            String role = jwtUtil.extractRole(token);
            potService.deletePot(id, userId, role);

            Map<String, Object> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "Pot deleted successfully");
            response.put("data", null);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = getErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = getErrorResponse("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllPotsByUserId(@PathVariable Long id) {
        String token = extractTokenFromHeader();
        Long authenticatedUserId = Long.valueOf(jwtUtil.extractUserId(token));
        String role = jwtUtil.extractRole(token);
        if (!role.equals("ROLE_ADMIN") && !authenticatedUserId.equals(id)) {
            Map<String, Object> response = getErrorResponse("You can only access your own pots");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        List<PotResponseDTO> pots = potService.getAllPotsByUser(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", "true");
        response.put("message", "Pots retrieved successfully");
        response.put("data", pots);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private String extractTokenFromHeader() {
        String authHeader = SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("JWT Token not found in request headers");
    }

    private static Map<String, Object> getErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", "false");
        response.put("message", message);
        response.put("data", null);
        return response;
    }
}