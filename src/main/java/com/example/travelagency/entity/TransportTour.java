package com.example.travelagency.entity;

import com.example.travelagency.Id.TransportTourId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "transport_tour")
public class TransportTour {

    @EmbeddedId
    private TransportTourId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tourId")
    @JoinColumn(name = "tour_id", referencedColumnName = "tour_id")
    @JsonIgnore
    private Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("transportId")
    @JoinColumn(name = "transport_id", referencedColumnName = "transport_id")
    @JsonIgnore
    private Transport transport;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "arrival_date", nullable = false)
    private LocalDate arrivalDate;

    // Getters and Setters (автоматически генерируются Lombok)
}