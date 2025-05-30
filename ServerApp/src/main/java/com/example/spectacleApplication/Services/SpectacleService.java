package com.example.spectacleApplication.Services;

import com.example.spectacleApplication.DTO.*;
import com.example.spectacleApplication.Repositories.LieuRepository;
import com.example.spectacleApplication.Repositories.SpectacleRepository;
import com.example.spectacleApplication.entity.Spectacle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpectacleService {

    @Autowired
    private SpectacleRepository spectacleRepository;

    @Autowired
    private LieuRepository lieuRepository;

    public List<SpectacleDTO> getAllSpectacles() {
        return spectacleRepository.findAll().stream().map(spectacle -> {
            SpectacleDTO dto = new SpectacleDTO();
            dto.setId(spectacle.getId());
            dto.setTitre(spectacle.getTitre());
            dto.setLieuNom(spectacle.getLieu().getNomLieu());
            dto.setVille(spectacle.getLieu().getAdresse());
            dto.setImageURL(spectacle.getImageURL());
            return dto;
        }).collect(Collectors.toList());
    }

    public SpectacleDetailsDTO getSpectacleDetails(Integer id) {
        Spectacle spectacle = spectacleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spectacle not found"));

        SpectacleDetailsDTO dto = new SpectacleDetailsDTO();
        dto.setId(spectacle.getId());
        dto.setTitre(spectacle.getTitre());
        dto.setDuree(spectacle.getDuree());
        dto.setProgrammations(spectacle.getProgrammations().stream()
                .map(programmation -> new ProgrammationDTO(
                        programmation.getId(),
                        programmation.getDate().toString(),
                        programmation.getHeure().toString(),
                        programmation.getSpectacle().getId(),
                        programmation.getLieu().getId()
                ))
                .collect(Collectors.toList()));
        String googleMapsLink = String.format("https://www.google.com/maps?q=%f,%f",
                spectacle.getLieu().getLatitude(), spectacle.getLieu().getLongitude());
        dto.setGoogleMapsLink(googleMapsLink);
        dto.setDescription(spectacle.getDescription());
        dto.setPlacesDisponibles(spectacle.getNbPlacesDisponibles());
        dto.setImageUrl(spectacle.getImageURL());
        dto.setArtistes(spectacle.getArtistes().stream()
                .map(artiste -> new ArtisteDTO(artiste.getNom(), artiste.getPrenom(), artiste.getSpecialite()))
                .collect(Collectors.toList()));
        List<String> categorieNoms = spectacle.getCategories() != null && !spectacle.getCategories().isEmpty()
                ? spectacle.getCategories().stream()
                .map(categorie -> categorie.getNomCategorie() != null ? categorie.getNomCategorie() : "Inconnu")
                .collect(Collectors.toList())
                : List.of("Inconnu");
        dto.setCategorieNoms(categorieNoms);
        List<TicketTypeDTO> ticketTypes = spectacle.getSpectacleTickets() != null
                ? spectacle.getSpectacleTickets().stream()
                .map(spectacleTicket -> {
                    TicketTypeDTO ticketTypeDTO = new TicketTypeDTO();
                    ticketTypeDTO.setName(spectacleTicket.getTicketType().getName());
                    ticketTypeDTO.setPrice(spectacleTicket.getPrice());
                    return ticketTypeDTO;
                })
                .collect(Collectors.toList())
                : new ArrayList<>();
        dto.setTicketTypes(ticketTypes);
        return dto;
    }
}