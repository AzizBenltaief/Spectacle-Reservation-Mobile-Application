package com.example.spectacleApplication.DTO;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpectacleDetailsDTO {
    private Integer id;
    private String titre;
    private List<ProgrammationDTO> programmations;
    private String googleMapsLink;
    private String description;
    private Integer placesDisponibles;
    private List<ArtisteDTO> artistes;
    private String imageUrl;
    private List<String> categorieNoms;
    private Float duree;
    @JsonProperty("ticketTypes")
    private List<TicketTypeDTO> ticketTypes;
    public Float getDuree() {
        return duree;
    }

    public List<TicketTypeDTO> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketTypeDTO> ticketTypes) {
        this.ticketTypes = ticketTypes;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<ProgrammationDTO> getProgrammations() {
        return programmations;
    }

    public void setProgrammations(List<ProgrammationDTO> programmations) {
        this.programmations = programmations;
    }

    public String getGoogleMapsLink() {
        return googleMapsLink;
    }

    public void setGoogleMapsLink(String googleMapsLink) {
        this.googleMapsLink = googleMapsLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(Integer placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public List<ArtisteDTO> getArtistes() {
        return artistes;
    }

    public void setArtistes(List<ArtisteDTO> artistes) {
        this.artistes = artistes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageURL) {
        this.imageUrl = imageURL;
    }
}
