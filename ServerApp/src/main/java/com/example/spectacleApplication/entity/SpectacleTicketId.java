package com.example.spectacleApplication.entity;


import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SpectacleTicketId implements Serializable {
    private Integer spectacleId;
    private Integer ticketTypeId;

    // Getters, Setters, equals(), and hashCode()
    public Integer getSpectacleId() { return spectacleId; }
    public void setSpectacleId(Integer spectacleId) { this.spectacleId = spectacleId; }
    public Integer getTicketTypeId() { return ticketTypeId; }
    public void setTicketTypeId(Integer ticketTypeId) { this.ticketTypeId = ticketTypeId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpectacleTicketId that = (SpectacleTicketId) o;
        return spectacleId.equals(that.spectacleId) && ticketTypeId.equals(that.ticketTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spectacleId, ticketTypeId);
    }
}