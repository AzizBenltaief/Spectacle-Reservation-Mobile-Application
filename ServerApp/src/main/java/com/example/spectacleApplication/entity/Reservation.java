package com.example.spectacleApplication.entity;


import jakarta.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "programmation_id", nullable = false)
    private Programmation programmation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "number_of_places", nullable = false)
    private Integer numberOfPlaces;

    @Column(name = "categorie_billet")
    private String categorieBillet;


    // Constructors
    public Reservation() {}

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Programmation getProgrammation() {
        return programmation;
    }

    public void setProgrammation(Programmation programmation) {
        this.programmation = programmation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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


}
