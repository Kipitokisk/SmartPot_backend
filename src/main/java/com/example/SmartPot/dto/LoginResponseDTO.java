package com.example.SmartPot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class LoginResponseDTO {
    private String token;
    private String email;
    private String role;
}