package com.example.spectacleApplication.Controllers;

import com.example.spectacleApplication.DTO.SpectacleDTO;
import com.example.spectacleApplication.DTO.SpectacleDetailsDTO;
import com.example.spectacleApplication.Repositories.LieuRepository;
import com.example.spectacleApplication.Services.SpectacleService;
import com.example.spectacleApplication.entity.Lieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spectacles")
public class SpectacleController {
    @Autowired
    private SpectacleService spectacleService;

    @GetMapping
    public List<SpectacleDTO> getAllSpectacles() {
        return spectacleService.getAllSpectacles();
    }

    @GetMapping("/{id}")
    public SpectacleDetailsDTO getSpectacleDetails(@PathVariable Integer id) {
        return spectacleService.getSpectacleDetails(id);
    }

    @Autowired
    private LieuRepository lieuRepository;

    @GetMapping("/lieux")
    public ResponseEntity<List<String>> getAllLieux() {
        List<Lieu> lieux = lieuRepository.findAll();
        List<String> lieuNoms = lieux.stream()
                .filter(lieu -> lieu.getNomLieu() != null)
                .map(Lieu::getNomLieu)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(lieuNoms);
    }
}
