package com.example.SmartPot.controller;

import com.example.SmartPot.dto.AuthDTO;
import com.example.SmartPot.dto.LoginResponseDTO;
import com.example.SmartPot.exceptions.ResourceAlreadyExistsException;
import com.example.SmartPot.exceptions.UserRegistrationException;
import com.example.SmartPot.model.User;
import com.example.SmartPot.repository.UserRepository;
import com.example.SmartPot.config.JwtUtil;
import com.example.SmartPot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (UserRegistrationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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

                return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token, user.getEmail(), user.getRole().name()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}