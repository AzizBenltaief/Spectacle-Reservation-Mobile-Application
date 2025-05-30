package com.example.spectacleApplication.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BilletDTO {
    private Integer id;
    private Integer programmationId;
    private Integer spectacleId; // Added
    private String spectacleTitre; // Added
    private Integer userId;
    private String categorieBillet;
    private Float prix;

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
