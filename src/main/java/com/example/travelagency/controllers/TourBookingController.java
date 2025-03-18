package com.example.travelagency.controllers;

import com.example.travelagency.Id.TourBookingId;
import com.example.travelagency.entity.TourBooking;
import com.example.travelagency.service.TourBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tour-bookings")
public class TourBookingController {

    private final TourBookingService tourBookingService;

    @Autowired
    public TourBookingController(TourBookingService tourBookingService) {
        this.tourBookingService = tourBookingService;
    }

    @GetMapping
    public ResponseEntity<List<TourBooking>> getAllTourBookings() {
        List<TourBooking> tourBookings = tourBookingService.getAllTourBookings();
        return new ResponseEntity<>(tourBookings, HttpStatus.OK);
    }

    @GetMapping("/{tourId}/{bookingId}")
    public ResponseEntity<TourBooking> getTourBookingById(@PathVariable Long tourId, @PathVariable Long bookingId) {
        TourBookingId id = new TourBookingId(tourId, bookingId);
        Optional<TourBooking> tourBooking = tourBookingService.getTourBookingById(id);
        return tourBooking.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TourBooking> createTourBooking(@RequestBody TourBooking tourBooking) {
        TourBooking createdTourBooking = tourBookingService.createTourBooking(tourBooking);
        return new ResponseEntity<>(createdTourBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{tourId}/{bookingId}")
    public ResponseEntity<TourBooking> updateTourBooking(@PathVariable Long tourId, @PathVariable Long bookingId, @RequestBody TourBooking tourBookingDetails) {
        TourBookingId id = new TourBookingId(tourId, bookingId);
        TourBooking updatedTourBooking = tourBookingService.updateTourBooking(id, tourBookingDetails);
        return new ResponseEntity<>(updatedTourBooking, HttpStatus.OK);
    }

    @DeleteMapping("/{tourId}/{bookingId}")
    public ResponseEntity<Void> deleteTourBooking(@PathVariable Long tourId, @PathVariable Long bookingId) {
        TourBookingId id = new TourBookingId(tourId, bookingId);
        tourBookingService.deleteTourBooking(id);
        return ResponseEntity.noContent().build();
    }
}