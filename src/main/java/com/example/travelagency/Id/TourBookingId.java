package com.example.travelagency.Id;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TourBookingId implements Serializable {

    private Long tourId;
    private Long bookingId;

    public TourBookingId() {}

    public TourBookingId(Long tourId, Long bookingId) {
        this.tourId = tourId;
        this.bookingId = bookingId;
    }

    // Getters and Setters

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourBookingId that)) return false;
        return Objects.equals(tourId, that.tourId) && Objects.equals(bookingId, that.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourId, bookingId);
    }
}