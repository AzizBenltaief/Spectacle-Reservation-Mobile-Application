package com.example.spectacleApplication.DTO;


public class ProgrammationDTO {
    private int id;

    private String date;

    private String heure;

    private int spectacleId;

    private int lieuId;

    // Constructeurs
    public ProgrammationDTO() {}

    public ProgrammationDTO(String date, String heure) {
        this.date = date;
        this.heure = heure;
    }

    public ProgrammationDTO(int id, String date, String heure, int spectacleId, int lieuId) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.spectacleId = spectacleId;
        this.lieuId = lieuId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public int getSpectacleId() {
        return spectacleId;
    }

    public void setSpectacleId(int spectacleId) {
        this.spectacleId = spectacleId;
    }

    public int getLieuId() {
        return lieuId;
    }

    public void setLieuId(int lieuId) {
        this.lieuId = lieuId;
    }
}

