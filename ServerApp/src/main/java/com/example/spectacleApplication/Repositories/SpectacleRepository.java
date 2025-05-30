package com.example.spectacleApplication.Repositories;

import com.example.spectacleApplication.entity.Spectacle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpectacleRepository extends JpaRepository<Spectacle, Integer> {
    @Query("SELECT s FROM Spectacle s LEFT JOIN FETCH s.categories WHERE s.id = :id")
    Optional<Spectacle> findByIdWithCategories(Integer id);
}
