package com.example.spectacleApplication.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;




@Entity
public class Programmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate date;
    private LocalTime heure;

    @ManyToOne
    @JoinColumn(name = "spectacle_id")
    private Spectacle spectacle;

    @ManyToOne
    @JoinColumn(name = "lieu_id")
    private Lieu lieu;

    // Constructeurs
    public Programmation() {}

    public Programmation(LocalDate date, LocalTime heure, Spectacle spectacle, Lieu lieu) {
        this.date = date;
        this.heure = heure;
        this.spectacle = spectacle;
        this.lieu = lieu;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public Spectacle getSpectacle() {
        return spectacle;
    }

    public void setSpectacle(Spectacle spectacle) {
        this.spectacle = spectacle;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }
}