package com.example.SmartPot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PotResponseDTO {
    private Long id;
    private Long userId;
    private Long plantId;
    private Integer currentMoisture;
    private Integer waterReservoir;
}
