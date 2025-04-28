package com.example.SmartPot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "system_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer maxWateringTimeSeconds;

    private Integer minCoolingPeriodMinutes;

    private Integer wateringCycleMinutes;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
