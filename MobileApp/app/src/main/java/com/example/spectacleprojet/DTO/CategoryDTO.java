package com.example.spectacleprojet.DTO;


import com.google.gson.annotations.SerializedName;

public class CategoryDTO {
    @SerializedName("id")
    private Integer id;

    @SerializedName("nom")
    private String nom;

    // Getters and setters
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
}
