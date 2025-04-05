package com.example.travelagency.controllers;

import com.example.travelagency.Id.TransportTourId;
import com.example.travelagency.dtos.TransportTourDTO;
import com.example.travelagency.entity.TransportTour;
import com.example.travelagency.service.TransportTourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transport_tour")
public class TransportTourController {

    private final TransportTourService transportTourService;

    @Autowired
    public TransportTourController(TransportTourService transportTourService) {
        this.transportTourService = transportTourService;
    }

    @GetMapping
    public ResponseEntity<List<TransportTourDTO>> getAllTransportTours() {
        List<TransportTourDTO> transportTours = transportTourService.getAllTransportObjects();
        return new ResponseEntity<>(transportTours, HttpStatus.OK);
    }

    @GetMapping("/{tourId}/{transportId}")
    public ResponseEntity<TransportTour> getTransportTourById(@PathVariable Long tourId, @PathVariable Long transportId) {
        TransportTourId id = new TransportTourId(tourId, transportId);
        Optional<TransportTour> transportTour = transportTourService.getTransportTourById(id);
        return transportTour.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TransportTour> createTransportTour(@RequestBody TransportTour transportTour) {
        TransportTour createdTransportTour = transportTourService.createTransportTour(transportTour);
        return new ResponseEntity<>(createdTransportTour, HttpStatus.CREATED);
    }

    @PutMapping("/{tourId}/{transportId}")
    public ResponseEntity<TransportTour> updateTransportTour(@PathVariable Long tourId, @PathVariable Long transportId, @RequestBody TransportTour transportTourDetails) {
        TransportTourId id = new TransportTourId(tourId, transportId);
        TransportTour updatedTransportTour = transportTourService.updateTransportTour(id, transportTourDetails);
        return new ResponseEntity<>(updatedTransportTour, HttpStatus.OK);
    }

    @DeleteMapping("/{tourId}/{transportId}")
    public ResponseEntity<Void> deleteTransportTour(@PathVariable Long tourId, @PathVariable Long transportId) {
        TransportTourId id = new TransportTourId(tourId, transportId);
        transportTourService.deleteTransportTour(id);
        return ResponseEntity.noContent().build();
    }
}