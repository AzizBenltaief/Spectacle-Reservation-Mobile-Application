package com.example.spectacleApplication.Repositories;

import com.example.spectacleApplication.entity.Programmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgrammationRepository extends JpaRepository<Programmation, Integer> { }
