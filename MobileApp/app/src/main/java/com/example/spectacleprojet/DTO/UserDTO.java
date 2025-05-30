package com.example.spectacleprojet.DTO;


public class UserDTO {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Integer numTel;
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getNumTel() { return numTel; }
    public void setNumTel(Integer numTel) { this.numTel = numTel; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
