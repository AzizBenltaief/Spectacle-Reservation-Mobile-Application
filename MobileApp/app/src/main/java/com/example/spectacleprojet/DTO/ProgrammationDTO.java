package com.example.spectacleprojet.DTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProgrammationDTO {
    private Integer id;
    private String date;
    private String heure;
    private Integer placesDisponibles;

    private  LieuDTO lieu;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }
    public Integer getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(Integer placesDisponibles) { this.placesDisponibles = placesDisponibles; }

    public LieuDTO getLieu() {
        return lieu;
    }

    public void setLieu(LieuDTO lieu) {
        this.lieu = lieu;
    }

    @Override
    public String toString() {
        try {
            // Parse and format the date (e.g., "2025-04-25" to "25/04/2025")
            LocalDate localDate = LocalDate.parse(date);
            String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            // Trim seconds from heure (e.g., "20:00:00" to "20:00")
            String formattedHeure = heure != null && heure.length() >= 5 ? heure.substring(0, 5) : heure;
            int places = placesDisponibles != null ? placesDisponibles : 0;
            return String.format("%s %s ", formattedDate, formattedHeure);
        } catch (Exception e) {
            // Fallback if parsing fails
            String formattedHeure = heure != null && heure.length() >= 5 ? heure.substring(0, 5) : heure;
            int places = placesDisponibles != null ? placesDisponibles : 0;
            return String.format("%s %s (%d places)", date != null ? date : "N/A", formattedHeure != null ? formattedHeure : "N/A", places);
        }
    }
}