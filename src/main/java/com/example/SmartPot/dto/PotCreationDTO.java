package com.example.SmartPot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PotCreationDTO {
    private Long userId;
    private Long plantId;
    private Integer currentMoisture;
    private Integer waterReservoir;
}
