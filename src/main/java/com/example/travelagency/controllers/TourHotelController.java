package com.example.travelagency.controllers;

import com.example.travelagency.Id.TourHotelId;
import com.example.travelagency.entity.TourHotel;
import com.example.travelagency.service.TourHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tour_hotel")
public class TourHotelController {

    private final TourHotelService tourHotelService;

    @Autowired
    public TourHotelController(TourHotelService tourHotelService) {
        this.tourHotelService = tourHotelService;
    }

    @GetMapping
    public ResponseEntity<List<TourHotelId>> getAllTourHotels() {
        List<TourHotelId> tourHotels = tourHotelService.getAllTourHotelId();
        return new ResponseEntity<>(tourHotels, HttpStatus.OK);
    }

    @GetMapping("/{tourId}/{hotelId}")
    public ResponseEntity<TourHotel> getTourHotelById(@PathVariable Long tourId, @PathVariable Long hotelId) {
        TourHotelId id = new TourHotelId(tourId, hotelId);
        Optional<TourHotel> tourHotel = tourHotelService.getTourHotelById(id);
        return tourHotel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TourHotel> createTourHotel(@RequestBody TourHotel tourHotel) {
        TourHotel createdTourHotel = tourHotelService.createTourHotel(tourHotel);
        return new ResponseEntity<>(createdTourHotel, HttpStatus.CREATED);
    }

    @PutMapping("/{tourId}/{hotelId}")
    public ResponseEntity<TourHotel> updateTourHotel(@PathVariable Long tourId, @PathVariable Long hotelId, @RequestBody TourHotel tourHotelDetails) {
        TourHotelId id = new TourHotelId(tourId, hotelId);
        TourHotel updatedTourHotel = tourHotelService.updateTourHotel(id, tourHotelDetails);
        return new ResponseEntity<>(updatedTourHotel, HttpStatus.OK);
    }

    @DeleteMapping("/{tourId}/{hotelId}")
    public ResponseEntity<Void> deleteTourHotel(@PathVariable Long tourId, @PathVariable Long hotelId) {
        TourHotelId id = new TourHotelId(tourId, hotelId);
        tourHotelService.deleteTourHotel(id);
        return ResponseEntity.noContent().build();
    }
}