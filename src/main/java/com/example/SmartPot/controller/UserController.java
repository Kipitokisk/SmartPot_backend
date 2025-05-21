package com.example.SmartPot.controller;

import com.example.SmartPot.exceptions.ResourceAlreadyExistsException;
import com.example.SmartPot.exceptions.ResourceNotFoundException;
import com.example.SmartPot.model.User;
import com.example.SmartPot.repository.UserRepository;
import com.example.SmartPot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "User retrieved");
            response.put("data", user);


            return ResponseEntity.status(HttpStatus.FOUND).body(response);
        } catch (ResourceAlreadyExistsException e) {
            Map<String, Object> response = getErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            Map<String, Object> response = getErrorResponse("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    private static Map<String, Object> getErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", "false");
        response.put("message", message);
        response.put("data", null);
        return response;
    }

    public String extractMessage(String errorMessage) {
        try {
            if (errorMessage.contains("interpolatedMessage='")) {
                String readableMessage = errorMessage.split("interpolatedMessage='")[1].split("'")[0];
                return readableMessage;
            }
            return errorMessage;
        } catch (Exception e) {
            return "Error parsing response: " + errorMessage;
        }
    }
}
