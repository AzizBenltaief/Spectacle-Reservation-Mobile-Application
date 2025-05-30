package com.example.spectacleApplication.Services;


import com.example.spectacleApplication.DTO.BilletDTO;
import com.example.spectacleApplication.DTO.BilletResponseDTO;
import com.example.spectacleApplication.Repositories.BilletRepository;
import com.example.spectacleApplication.Repositories.SpectacleRepository;
import com.example.spectacleApplication.entity.Billet;
import com.example.spectacleApplication.entity.Spectacle;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BilletService {
    @Autowired
    private BilletRepository billetRepository;

    @Autowired
    private SpectacleRepository spectacleRepository;

    public List<BilletDTO> getReservationsByUserId(Integer userId) {
        List<Billet> billets = billetRepository.findByUserId(userId);
        return billets.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private BilletDTO mapToDTO(Billet billet) {
        BilletDTO billetDTO = new BilletDTO();
        billetDTO.setId(billet.getId());
        billetDTO.setProgrammationId(billet.getProgrammation().getId());
        billetDTO.setSpectacleId(billet.getProgrammation().getSpectacle().getId());
        billetDTO.setSpectacleTitre(billet.getProgrammation().getSpectacle().getTitre());
        billetDTO.setUserId(billet.getUser().getId());
        billetDTO.setCategorieBillet(billet.getCategorie());
        billetDTO.setPrix(billet.getPrix());
        return billetDTO;
    }

    @Transactional
    public boolean cancelBillet(Integer billetId) {
        Optional<Billet> billetOpt = billetRepository.findById(billetId);
        if (billetOpt.isEmpty()) {
            return false;
        }

        Billet billet = billetOpt.get();
        // Increment placesDisponibles in the associated spectacle
        Spectacle spectacle = billet.getProgrammation().getSpectacle();
        if (spectacle != null) {
            spectacle.setNbPlacesDisponibles(spectacle.getNbPlacesDisponibles() + 1);
            spectacleRepository.save(spectacle);
        }

        // Delete the billet
        billetRepository.delete(billet);
        return true;
    }
    public List<BilletResponseDTO> getBilletDetailsByUserId(Integer userId) {
        List<Object[]> results = billetRepository.findBilletDetailsByUserId(userId);
        return results.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    private BilletResponseDTO mapToResponseDTO(Object[] result) {
        BilletResponseDTO dto = new BilletResponseDTO();
        dto.setId((Integer) result[0]); // id
        dto.setSpectacleTitre((String) result[1]); // spectacle.titre
        dto.setCategorieBillet((String) result[2]); // categorieBillet
        dto.setPrix((Float) result[3]); // prix

        LocalDate date = (LocalDate) result[4];
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM", java.util.Locale.FRENCH);
        dto.setDate(date != null ? date.format(dateFormatter).toUpperCase() : "N/A");

        LocalTime heure = (LocalTime) result[5];
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dto.setHeure(heure != null ? heure.format(timeFormatter) : "N/A");

        return dto;
    }
}