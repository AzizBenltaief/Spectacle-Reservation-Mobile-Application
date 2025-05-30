package com.example.spectacleApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpectacleDTO {
    private Integer id; private String titre; private String lieuNom; private String ville; private String imageURL; }