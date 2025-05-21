package com.example.SmartPot.service;

import com.example.SmartPot.dto.AuthDTO;
import com.example.SmartPot.exceptions.ResourceNotFoundException;
import com.example.SmartPot.exceptions.ResourceAlreadyExistsException;
import com.example.SmartPot.exceptions.UserRegistrationException;
import com.example.SmartPot.model.Role;
import com.example.SmartPot.model.User;
import com.example.SmartPot.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(AuthDTO user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("User with " + user.getEmail() + " already exists!");
        }

        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            User newUser = User.builder()
                    .email(user.getEmail())
                    .password(encodedPassword)
                    .role(Role.USER)
                    .deviceToken(user.getDeviceToken())
                    .pots(new ArrayList<>())
                    .build();
            return userRepository.save(newUser);
        } catch (DataAccessException e) {
            throw new UserRegistrationException("Could not save user with email: " + user.getEmail(), e);
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found."));
    }
}
