package com.example.spectacleApplication.Controllers;

import com.example.spectacleApplication.DTO.BilletDTO;
import com.example.spectacleApplication.DTO.ReservationDTO;
import com.example.spectacleApplication.Services.ReservationService;
import com.example.spectacleApplication.entity.Billet;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        try {
            BilletDTO billet = reservationService.createReservation(reservationDTO);
            return ResponseEntity.ok(billet);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating reservation: " + e.getMessage());
        }
    }

}
