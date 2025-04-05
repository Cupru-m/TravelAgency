package com.example.travelagency.controllers;

import com.example.travelagency.entity.Transport;
import com.example.travelagency.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transport")
public class TransportController {

    private final TransportService transportService;

    @Autowired
    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping
    public ResponseEntity<List<Transport>> getAllTransports() {
        List<Transport> transports = transportService.getAllTransports();
        return new ResponseEntity<>(transports, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transport> getTransportById(@PathVariable Integer id) {
        Optional<Transport> transport = transportService.getTransportById(id);
        return transport.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transport> createTransport(@RequestBody Transport transport) {
        Transport createdTransport = transportService.createTransport(transport);
        return new ResponseEntity<>(createdTransport, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transport> updateTransport(@PathVariable Integer id, @RequestBody Transport transportDetails) {
        Transport updatedTransport = transportService.updateTransport(id, transportDetails);
        return new ResponseEntity<>(updatedTransport, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable Integer id) {
        transportService.deleteTransport(id);
        return ResponseEntity.noContent().build();
    }
}