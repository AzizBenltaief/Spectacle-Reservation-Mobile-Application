package com.example.spectacleApplication.entity;

import jakarta.persistence.*;

@Entity
public class Billet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "programmation_id", nullable = false)
    private Programmation programmation;

    @ManyToOne  // Changed from basic Integer field
    @JoinColumn(name = "spectacle_id", nullable = false, insertable = false, updatable = false)
    private Spectacle spectacle;  // This matches the "mappedBy" in Spectacle

    @Column(name = "spectacle_id", nullable = false) // Keep this for direct ID access if needed
    private Integer spectacleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String categorie;

    private Float prix;

    // Constructors
    public Billet() {}

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Programmation getProgrammation() {
        return programmation;
    }

    public void setProgrammation(Programmation programmation) {
        this.programmation = programmation;
    }

    public Integer getSpectacleId() {
        return spectacleId;
    }

    public void setSpectacleId(Integer spectacleId) {
        this.spectacleId = spectacleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }
}