package com.example.SmartPot.controller;

import com.example.SmartPot.dto.AuthDTO;
import com.example.SmartPot.dto.LoginResponseDTO;
import com.example.SmartPot.exceptions.ResourceAlreadyExistsException;
import com.example.SmartPot.exceptions.UserRegistrationException;
import com.example.SmartPot.model.User;
import com.example.SmartPot.repository.UserRepository;
import com.example.SmartPot.config.JwtUtil;
import com.example.SmartPot.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository,
                          UserService userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody AuthDTO user) {
        try {
            User savedUser = userService.saveUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "Registration successful");
            response.put("data", savedUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResourceAlreadyExistsException e) {
            Map<String, Object> response = getErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (UserRegistrationException e) {
            Map<String, Object> response = getErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (RuntimeException e) {
            String message = extractMessage(e.getMessage());
            Map<String, Object> response = getErrorResponse(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            Optional<User> userOptional = userRepository.findUserByEmail(loginRequest.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String token = jwtUtil.generateToken(user.getId().toString(), user.getEmail(), user.getRole().name());

                LoginResponseDTO loginResponseDTO = new LoginResponseDTO(token, user.getEmail(), user.getRole().name());

                Map<String, Object> response = new HashMap<>();
                response.put("success", "true");
                response.put("message", "Registration successful");
                response.put("data", loginResponseDTO);

                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, Object> response = getErrorResponse("Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (BadCredentialsException e) {
            Map<String, Object> response = getErrorResponse("Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
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