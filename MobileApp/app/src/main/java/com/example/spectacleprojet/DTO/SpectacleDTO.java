package com.example.spectacleprojet.DTO;


public class SpectacleDTO {
    private Integer id;
    private String titre;
    private String lieuNom;
    private String ville;

    private String imageURL;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getLieuNom() { return lieuNom; }
    public void setLieuNom(String lieuNom) { this.lieuNom = lieuNom; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
