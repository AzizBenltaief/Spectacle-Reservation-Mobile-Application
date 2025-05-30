package com.example.spectacleApplication.Services;

import com.example.spectacleApplication.DTO.BilletDTO;
import com.example.spectacleApplication.DTO.ReservationDTO;
import com.example.spectacleApplication.Repositories.BilletRepository;
import com.example.spectacleApplication.Repositories.ProgrammationRepository;
import com.example.spectacleApplication.Repositories.SpectacleRepository;
import com.example.spectacleApplication.Repositories.UserRepository;
import com.example.spectacleApplication.entity.Billet;
import com.example.spectacleApplication.entity.Programmation;
import com.example.spectacleApplication.entity.Spectacle;
import com.example.spectacleApplication.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

@Service
public class ReservationService {
    @Autowired
    private BilletRepository billetRepository;

    @Autowired
    private SpectacleRepository spectacleRepository;

    @Autowired
    private ProgrammationRepository programmationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public BilletDTO createReservation(ReservationDTO reservationDTO) {
        System.out.println("Creating reservation for programmationId: " + reservationDTO.getProgrammationId());

        Programmation programmation = programmationRepository.findById(reservationDTO.getProgrammationId())
                .orElseThrow(() -> new RuntimeException("Programmation not found"));
        System.out.println("Programmation found: " + programmation.getId());

        Spectacle spectacle = programmation.getSpectacle();
        if (spectacle == null) {
            throw new RuntimeException("Spectacle not found for the given programmation");
        }
        System.out.println("Spectacle found: " + spectacle.getTitre());

        User user = userRepository.findById(reservationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User found: " + user.getEmail());

        Integer nbPlacesDisponibles = spectacle.getNbPlacesDisponibles();
        Integer numberOfPlaces = reservationDTO.getNumberOfPlaces();
        if (nbPlacesDisponibles == null || nbPlacesDisponibles < numberOfPlaces) {
            throw new RuntimeException("Not enough available seats for this spectacle. Available: " + (nbPlacesDisponibles != null ? nbPlacesDisponibles : 0));
        }

        spectacle.setNbPlacesDisponibles(nbPlacesDisponibles - numberOfPlaces);
        spectacleRepository.save(spectacle);
        System.out.println("Updated available seats: " + spectacle.getNbPlacesDisponibles());

        Billet billet = new Billet();
        billet.setProgrammation(programmation);
        billet.setSpectacleId(spectacle.getId());
        billet.setUser(user);
        billet.setCategorie(reservationDTO.getCategorieBillet());
        billet.setPrix(reservationDTO.getPrix());

        Billet savedBillet = billetRepository.save(billet);
        System.out.println("Billet saved with ID: " + savedBillet.getId());

        // Send email confirmation
        try {
            System.out.println("Attempting to send email to: " + user.getEmail());
            emailService.sendReservationConfirmationEmail(
                    user.getEmail(),
                    spectacle.getTitre(),
                    programmation.getDate().toString(),
                    programmation.getHeure().toString(),
                    reservationDTO.getNumberOfPlaces(),
                    reservationDTO.getCategorieBillet(),
                    reservationDTO.getPrix(),
                    savedBillet.getId()
            );
            System.out.println("Email sent successfully to: " + user.getEmail());
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + user.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        }

        BilletDTO billetDTO = new BilletDTO();
        billetDTO.setId(savedBillet.getId());
        billetDTO.setProgrammationId(savedBillet.getProgrammation().getId());
        billetDTO.setSpectacleId(savedBillet.getSpectacleId());
        billetDTO.setSpectacleTitre(spectacle.getTitre());
        billetDTO.setUserId(savedBillet.getUser().getId());
        billetDTO.setCategorieBillet(savedBillet.getCategorie());
        billetDTO.setPrix(savedBillet.getPrix());

        return billetDTO;
    }
}