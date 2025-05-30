package com.example.spectacleApplication.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketTypeDTO {
    private String name;
    @JsonProperty("price")
    private Float price;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }
}
