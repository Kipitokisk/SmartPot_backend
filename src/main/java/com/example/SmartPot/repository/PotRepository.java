package com.example.SmartPot.repository;

import com.example.SmartPot.model.Pot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PotRepository extends JpaRepository<Pot, Long> {
    List<Pot> findAllByUser_Id(Long userId);
}
