package com.example.spectacleApplication.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BilletResponseDTO {
    private int id;
    private String spectacleTitre;
    private String categorieBillet;
    private Float prix;
    private String date;
    private String heure;
}
