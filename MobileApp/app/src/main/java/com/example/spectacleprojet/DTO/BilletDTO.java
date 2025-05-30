package com.example.spectacleprojet.DTO;


import com.google.gson.annotations.SerializedName;

public class BilletDTO {
    @SerializedName("id")
    private Integer id;

    @SerializedName("programmationId")
    private Integer programmationId;

    @SerializedName("spectacleId")
    private Integer spectacleId; // Added

    @SerializedName("spectacleTitre")
    private String spectacleTitre; // Added

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("categorieBillet")
    private String categorieBillet;

    @SerializedName("prix")
    private Float prix;

    // Constructors
    public BilletDTO() {}

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProgrammationId() {
        return programmationId;
    }

    public void setProgrammationId(Integer programmationId) {
        this.programmationId = programmationId;
    }

    public Integer getSpectacleId() {
        return spectacleId;
    }

    public void setSpectacleId(Integer spectacleId) {
        this.spectacleId = spectacleId;
    }

    public String getSpectacleTitre() {
        return spectacleTitre;
    }

    public void setSpectacleTitre(String spectacleTitre) {
        this.spectacleTitre = spectacleTitre;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public void setPrix(Float prix) {
        this.prix = prix;
    }
}
