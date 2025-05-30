package com.example.spectacleprojet.DTO;




import com.google.gson.annotations.SerializedName;

public class TicketTypeDTO {
    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private Float price;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    // Optional: Override toString() for debugging
    @Override
    public String toString() {
        return "TicketTypeDTO{name='" + name + "', price=" + price + "}";
    }
}
