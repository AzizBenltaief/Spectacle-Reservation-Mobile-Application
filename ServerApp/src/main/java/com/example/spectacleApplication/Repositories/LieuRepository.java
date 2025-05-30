package com.example.spectacleApplication.Repositories;

import com.example.spectacleApplication.entity.Lieu;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LieuRepository extends JpaRepository<Lieu, Integer> {

    @Query("SELECT DISTINCT l.nomLieu FROM Lieu l WHERE l.nomLieu IS NOT NULL")
    List<String> findAllLieuNoms();
}
