package com.example.SmartPot.repository;

import com.example.SmartPot.model.Pot;
import com.example.SmartPot.model.WateringEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WateringEventRepository extends JpaRepository<WateringEvent, Long> {
    List<WateringEvent> findByPotId(Long potId);
}
