package com.example.travelagency.controllers;

import com.example.travelagency.Id.TourGuideId;
import com.example.travelagency.entity.TourGuide;
import com.example.travelagency.service.TourGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tour_guide")
public class TourGuideController {

    private final TourGuideService tourGuideService;

    @Autowired
    public TourGuideController(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

    @GetMapping
    public ResponseEntity<List<TourGuideId>> getAllTourGuides() {
        List<TourGuideId> tourGuides = tourGuideService.getAllTourGiudeId();
        return new ResponseEntity<>(tourGuides, HttpStatus.OK);
    }

    @GetMapping("/{tourId}/{guideId}")
    public ResponseEntity<TourGuide> getTourGuideById(@PathVariable Long tourId, @PathVariable Long guideId) {
        TourGuideId id = new TourGuideId(tourId, guideId);
        Optional<TourGuide> tourGuide = tourGuideService.getTourGuideById(id);
        return tourGuide.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TourGuide> createTourGuide(@RequestBody TourGuide tourGuide) {
        TourGuide createdTourGuide = tourGuideService.createTourGuide(tourGuide);
        return new ResponseEntity<>(createdTourGuide, HttpStatus.CREATED);
    }

    @PutMapping("/{tourId}/{guideId}")
    public ResponseEntity<TourGuide> updateTourGuide(@PathVariable Long tourId, @PathVariable Long guideId, @RequestBody TourGuide tourGuideDetails) {
        TourGuideId id = new TourGuideId(tourId, guideId);
        TourGuide updatedTourGuide = tourGuideService.updateTourGuide(id, tourGuideDetails);
        return new ResponseEntity<>(updatedTourGuide, HttpStatus.OK);
    }

    @DeleteMapping("/{tourId}/{guideId}")
    public ResponseEntity<Void> deleteTourGuide(@PathVariable Long tourId, @PathVariable Long guideId) {
        TourGuideId id = new TourGuideId(tourId, guideId);
        tourGuideService.deleteTourGuide(id);
        return ResponseEntity.noContent().build();
    }
}