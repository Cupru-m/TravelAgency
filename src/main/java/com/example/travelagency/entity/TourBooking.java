package com.example.travelagency.entity;

import com.example.travelagency.Id.TourBookingId;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tour_booking")
public class TourBooking {

    @EmbeddedId
    private TourBookingId id;

    @ManyToOne
    @MapsId("tourId")
    @JoinColumn(name = "tour_id", referencedColumnName = "id_тура")
    private Tour tour;

    @ManyToOne
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    // Getters and Setters
    // ...
}
