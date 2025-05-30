package com.example.spectacleApplication.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "spectacle_ticket")
public class SpectacleTicket {
    @EmbeddedId
    private SpectacleTicketId id;

    @ManyToOne
    @MapsId("spectacleId")
    @JoinColumn(name = "spectacle_id")
    private Spectacle spectacle;

    @ManyToOne
    @MapsId("ticketTypeId")
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;

    private Float price;

    // Getters and Setters
    public SpectacleTicketId getId() { return id; }
    public void setId(SpectacleTicketId id) { this.id = id; }
    public Spectacle getSpectacle() { return spectacle; }
    public void setSpectacle(Spectacle spectacle) { this.spectacle = spectacle; }
    public TicketType getTicketType() { return ticketType; }
    public void setTicketType(TicketType ticketType) { this.ticketType = ticketType; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    @Embeddable
    public static class SpectacleTicketId implements Serializable {
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
}