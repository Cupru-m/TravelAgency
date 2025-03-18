package com.example.travelagency.entity;

import com.example.travelagency.Id.TourHotelId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tour_hotel")
public class TourHotel {

    @EmbeddedId
    private TourHotelId id;

    @ManyToOne
    @MapsId("tourId")
    @JoinColumn(name = "tour_id", referencedColumnName = "id_тура")
    private Tour tour;

    @ManyToOne
    @MapsId("hotelId")
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;

}
