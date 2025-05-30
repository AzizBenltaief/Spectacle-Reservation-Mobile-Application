package com.example.spectacleApplication.Services;


import com.example.spectacleApplication.DTO.ProgrammationDTO;
import com.example.spectacleApplication.Repositories.ProgrammationRepository;
import com.example.spectacleApplication.entity.Programmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ProgrammationService {
    @Autowired
    private ProgrammationRepository programmationRepository;

    public ProgrammationDTO getProgrammationById(Integer id) {
        Optional<Programmation> programmationOpt = programmationRepository.findById(id);
        if (programmationOpt.isEmpty()) {
            return null;
        }

        Programmation programmation = programmationOpt.get();
        ProgrammationDTO programmationDTO = new ProgrammationDTO();
        programmationDTO.setId(programmation.getId());

        // Format date as "12 JUN"
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM", java.util.Locale.FRENCH);
        programmationDTO.setDate(programmation.getDate() != null ?
                programmation.getDate().format(dateFormatter).toUpperCase() : "N/A");

        // Format heure as "19:30"
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        programmationDTO.setHeure(programmation.getHeure() != null ?
                programmation.getHeure().format(timeFormatter) : "N/A");

        return programmationDTO;
    }
}
