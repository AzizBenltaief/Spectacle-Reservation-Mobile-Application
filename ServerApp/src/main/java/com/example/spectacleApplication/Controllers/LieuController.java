package com.example.spectacleApplication.Controllers;


import com.example.spectacleApplication.Repositories.LieuRepository;
import com.example.spectacleApplication.entity.Lieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lieux")
public class LieuController {

    @Autowired
    private LieuRepository lieuRepository;

    @GetMapping
    public ResponseEntity<List<String>> getAllLieux() {
        List<Lieu> lieux = lieuRepository.findAll();
        List<String> lieuNoms = lieux.stream()
                .map(Lieu::getNomLieu)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return ResponseEntity.ok(lieuNoms);
    }

}