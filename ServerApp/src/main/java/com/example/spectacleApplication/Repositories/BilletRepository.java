package com.example.spectacleApplication.Repositories;

import com.example.spectacleApplication.entity.Billet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BilletRepository extends JpaRepository<Billet, Integer> {
    List<Billet> findByUserId(Integer userId);

    @Query("SELECT b.id, b.spectacle.titre, b.categorie, b.prix, p.date, p.heure " +
            "FROM Billet b " +
            "JOIN b.programmation p " +
            "WHERE b.user.id = :userId")
    List<Object[]> findBilletDetailsByUserId(Integer userId);
}