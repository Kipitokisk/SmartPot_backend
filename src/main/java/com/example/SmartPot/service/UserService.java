package com.example.SmartPot.service;

import com.example.SmartPot.dto.AuthDTO;
import com.example.SmartPot.exceptions.UserAlreadyExistsException;
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
            throw new UserAlreadyExistsException("User with " + user.getEmail() + " already exists!");
        }

        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setPassword(encodedPassword);
            newUser.setRole(Role.USER);
            newUser.setPots(new ArrayList<>());
            return userRepository.save(newUser);
        } catch (DataAccessException e) {
            throw new UserRegistrationException("Could not save user with email: " + user.getEmail(), e);
        }
    }
}
