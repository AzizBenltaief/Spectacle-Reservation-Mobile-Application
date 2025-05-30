package com.example.spectacleApplication.Controllers;


import com.example.spectacleApplication.DTO.ProgrammationDTO;
import com.example.spectacleApplication.Services.ProgrammationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/programmations")
public class ProgrammationController {
    private static final Logger logger = Logger.getLogger(ProgrammationController.class.getName());

    @Autowired
    private ProgrammationService programmationService;

    @GetMapping("/{id}")
    public ResponseEntity<ProgrammationDTO> getProgrammationById(@PathVariable Integer id) {
        try {
            logger.info("Fetching programmation with ID: " + id);
            ProgrammationDTO programmation = programmationService.getProgrammationById(id);
            if (programmation == null) {
                logger.warning("Programmation not found with ID: " + id);
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.ok(programmation);
        } catch (Exception e) {
            logger.severe("Error fetching programmation with ID: " + id + " - " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
