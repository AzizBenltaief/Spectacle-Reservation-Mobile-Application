package com.example.spectacleApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Integer id;
    private Integer programmationId;
    private Integer spectacleId; // Added
    private Integer userId;
    private Integer numberOfPlaces;
    private String categorieBillet;
    private Float prix;
}