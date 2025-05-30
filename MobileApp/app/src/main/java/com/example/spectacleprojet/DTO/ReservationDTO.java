package com.example.spectacleprojet.DTO;

import com.google.gson.annotations.SerializedName;

public class ReservationDTO {
    @SerializedName("id")
    private Integer id;

    @SerializedName("programmationId")
    private Integer programmationId;

    @SerializedName("spectacleId")
    private Integer spectacleId; // Added

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("numberOfPlaces")
    private Integer numberOfPlaces;

    @SerializedName("categorieBillet")
    private String categorieBillet;

    @SerializedName("prix")
    private Float prix;

    // Constructors
    public ReservationDTO() {}

    public ReservationDTO(Integer programmationId, Integer spectacleId, Integer userId, Integer numberOfPlaces, String categorieBillet, Float prix) {
        this.programmationId = programmationId;
        this.spectacleId = spectacleId;
        this.userId = userId;
        this.numberOfPlaces = numberOfPlaces;
        this.categorieBillet = categorieBillet;
        this.prix = prix;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public void setNumberOfPlaces(Integer numberOfPlaces) {
        this.numberOfPlaces = numberOfPlaces;
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