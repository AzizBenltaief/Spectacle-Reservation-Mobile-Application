package com.example.spectacleApplication.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Artiste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;
    private String specialite;

    @ManyToMany(mappedBy = "artistes")
    private List<Spectacle> spectacles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public List<Spectacle> getSpectacles() {
        return spectacles;
    }

    public void setSpectacles(List<Spectacle> spectacles) {
        this.spectacles = spectacles;
    }
}
