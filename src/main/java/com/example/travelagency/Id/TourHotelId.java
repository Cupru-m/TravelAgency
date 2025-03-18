package com.example.travelagency.Id;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TourHotelId implements Serializable {

    private Long tourId;
    private Long hotelId;

    public TourHotelId() {}

    public TourHotelId(Long tourId, Long hotelId) {
        this.tourId = tourId;
        this.hotelId = hotelId;
    }

    // Getters and Setters

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourHotelId)) return false;
        TourHotelId that = (TourHotelId) o;
        return Objects.equals(tourId, that.tourId) && Objects.equals(hotelId, that.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourId, hotelId);
    }
}