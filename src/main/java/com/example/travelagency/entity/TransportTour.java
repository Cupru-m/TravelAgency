package com.example.travelagency.entity;

import com.example.travelagency.Id.TransportTourId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "transport_tour")
public class TransportTour {

    @EmbeddedId
    private TransportTourId id;

    @ManyToOne
    @MapsId("tourId")
    @JoinColumn(name = "tour_id", referencedColumnName = "id_тура")
    private Tour tour;

    @ManyToOne
    @MapsId("transportId")
    @JoinColumn(name = "transport_id", referencedColumnName = "id_транспорта")
    private Transport transport;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "arrival_date", nullable = false)
    private LocalDate arrivalDate;

    // Getters and Setters (автоматически генерируются Lombok)
}