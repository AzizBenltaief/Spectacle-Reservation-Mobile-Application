package com.example.spectacleApplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spectacle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titre;
    private Float duree;
    @Column(name = "imageurl") // Must match the database column name
    private String imageURL;
    private String description;
    private Integer nbPlacesDisponibles;

    @ManyToMany
    @JoinTable(
            name = "spectacle_categorie",
            joinColumns = @JoinColumn(name = "spectacle_id"),
            inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    private List<Categorie> categories;

    @ManyToOne
    @JoinColumn(name = "lieu_id")
    private Lieu lieu;

    @OneToMany(mappedBy = "spectacle")
    private List<Programmation> programmations;

    @ManyToMany
    @JoinTable(
            name = "participer",
            joinColumns = @JoinColumn(name = "spectacle_id"),
            inverseJoinColumns = @JoinColumn(name = "artiste_id")
    )
    private List<Artiste> artistes;

    @OneToMany(mappedBy = "spectacle")
    private List<Billet> billets;

    @OneToMany(mappedBy = "spectacle", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SpectacleTicket> spectacleTickets;
    public Integer getNbPlacesDisponibles() {
        return nbPlacesDisponibles;
    }

    public List<SpectacleTicket> getSpectacleTickets() {
        return spectacleTickets;
    }

    public void setSpectacleTickets(List<SpectacleTicket> spectacleTickets) {
        this.spectacleTickets = spectacleTickets;
    }

    public void setNbPlacesDisponibles(Integer nbPlacesDisponibles) {
        this.nbPlacesDisponibles = nbPlacesDisponibles;
    }

    public Float getDuree() {
        return duree;
    }

    public void setDuree(Float duree) {
        this.duree = duree;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public List<Programmation> getProgrammations() {
        return programmations;
    }

    public void setProgrammations(List<Programmation> programmations) {
        this.programmations = programmations;
    }

    public List<Artiste> getArtistes() {
        return artistes;
    }

    public void setArtistes(List<Artiste> artistes) {
        this.artistes = artistes;
    }

    public List<Billet> getBillets() {
        return billets;
    }

    public void setBillets(List<Billet> billets) {
        this.billets = billets;
    }
}