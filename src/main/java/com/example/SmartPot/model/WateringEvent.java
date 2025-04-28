package com.example.SmartPot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "watering_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WateringEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "pot_id")
    private Pot pot;
    private LocalDateTime timestamp;
    private Integer durationSeconds;
    @Enumerated(EnumType.STRING)
    private WateringMethod method;
}
