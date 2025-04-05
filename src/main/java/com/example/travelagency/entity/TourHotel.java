package com.example.travelagency.entity;

import com.example.travelagency.Id.TourHotelId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tour_hotel")
public class TourHotel {

    @EmbeddedId
    private TourHotelId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tourId")
    @JoinColumn(name = "tour_id", referencedColumnName = "tour_id")
    @JsonIgnore
    private Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("hotelId")
    @JsonIgnore
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;

}
