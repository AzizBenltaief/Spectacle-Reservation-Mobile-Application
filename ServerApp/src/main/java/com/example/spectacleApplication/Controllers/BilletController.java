package com.example.spectacleApplication.Controllers;

import com.example.spectacleApplication.DTO.BilletResponseDTO;
import com.example.spectacleApplication.Services.BilletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/reservations")
public class BilletController {
    private static final Logger logger = Logger.getLogger(BilletController.class.getName());

    @Autowired
    private BilletService billetService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BilletResponseDTO>> getBilletDetailsByUserId(@PathVariable Integer userId) {
        try {
            logger.info("Fetching billet details for user ID: " + userId);
            List<BilletResponseDTO> billetDetails = billetService.getBilletDetailsByUserId(userId);
            if (billetDetails.isEmpty()) {
                logger.info("No billet details found for user ID: " + userId);
                return ResponseEntity.ok(billetDetails); // Return 200 with empty list
            }
            return ResponseEntity.ok(billetDetails);
        } catch (Exception e) {
            logger.severe("Error fetching billet details for user ID: " + userId + " - " + e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{billetId}")
    public ResponseEntity<String> cancelBillet(@PathVariable Integer billetId) {
        try {
            logger.info("Cancelling billet with ID: " + billetId);
            boolean success = billetService.cancelBillet(billetId);
            if (success) {
                logger.info("Billet with ID " + billetId + " cancelled successfully");
                return ResponseEntity.ok("Billet cancelled successfully");
            } else {
                logger.warning("Billet with ID " + billetId + " not found");
                return ResponseEntity.status(404).body("Billet not found");
            }
        } catch (Exception e) {
            logger.severe("Error cancelling billet with ID: " + billetId + " - " + e.getMessage());
            return ResponseEntity.status(500).body("Error cancelling billet: " + e.getMessage());
        }
    }
}