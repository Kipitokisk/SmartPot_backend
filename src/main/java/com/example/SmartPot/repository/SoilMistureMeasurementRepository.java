package com.example.SmartPot.repository;

import com.example.SmartPot.model.SoilMoistureMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoilMistureMeasurementRepository extends JpaRepository<SoilMoistureMeasurement, Long> {
}
