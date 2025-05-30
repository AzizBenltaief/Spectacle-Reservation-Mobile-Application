package com.example.spectacleprojet.DTO;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpectacleDetailsDTO {
    private Integer id;
    private String titre;
    private List<ProgrammationDTO> programmations;
    private String googleMapsLink;
    private String description;
    private Integer placesDisponibles;
    private List<String> categorieNoms;    private List<ArtisteDTO> artistes;
    @SerializedName("imageUrl")
    private String imageURL;
    private Float duree;
    @SerializedName("ticketTypes")
    private List<TicketTypeDTO> ticketTypes;

    public List<TicketTypeDTO> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketTypeDTO> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitre() { return titre; }

    public Float getDuree() {
        return duree;
    }

    public void setDuree(Float duree) {
        this.duree = duree;
    }

    public List<String> getCategorieNoms() {
        return categorieNoms;
    }

    public void setCategorieNoms(List<String> categorieNoms) {
        this.categorieNoms = categorieNoms;
    }

    public void setTitre(String titre) { this.titre = titre; }
    public List<ProgrammationDTO> getProgrammations() { return programmations; }
    public void setProgrammations(List<ProgrammationDTO> programmations) { this.programmations = programmations; }
    public String getGoogleMapsLink() { return googleMapsLink; }
    public void setGoogleMapsLink(String googleMapsLink) { this.googleMapsLink = googleMapsLink; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(Integer placesDisponibles) { this.placesDisponibles = placesDisponibles; }
    public List<ArtisteDTO> getArtistes() { return artistes; }
    public void setArtistes(List<ArtisteDTO> artistes) { this.artistes = artistes; }
    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
    @Override
    public String toString() {
        return "SpectacleDetailsDTO{" +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", placesDisponibles=" + placesDisponibles +
                ", googleMapsLink='" + googleMapsLink + '\'' +
                ", programmations=" + programmations +
                ", artistes=" + artistes +
                '}';
    }
}