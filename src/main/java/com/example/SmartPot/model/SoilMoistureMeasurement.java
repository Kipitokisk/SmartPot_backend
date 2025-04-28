package com.example.SmartPot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "soil_moisture_measurements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoilMoistureMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pot_id")
    private Pot pot;

    private Integer moistureLevel; // value from sensor

    private LocalDateTime measuredAt;
}
