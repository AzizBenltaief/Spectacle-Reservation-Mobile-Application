package com.example.spectacleprojet.DTO;


import com.google.gson.annotations.SerializedName;

public class BilletResponseDTO {
    private int id;
    private String spectacleTitre;

    private String categorieBillet;

    private Float prix;

    private String date;

    private String heure;

    public String getSpectacleTitre() {
        return spectacleTitre;
    }

    public void setSpectacleTitre(String spectacleTitre) {
        this.spectacleTitre = spectacleTitre;
    }

    public String getCategorieBillet() {
        return categorieBillet;
    }

    public void setCategorieBillet(String categorieBillet) {
        this.categorieBillet = categorieBillet;
    }

    public Float getPrix() {
        return prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
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
}
