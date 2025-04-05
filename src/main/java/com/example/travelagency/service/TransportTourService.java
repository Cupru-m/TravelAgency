package com.example.travelagency.service;

import com.example.travelagency.Id.TransportTourId;
import com.example.travelagency.dtos.TransportTourDTO;
import com.example.travelagency.entity.TransportTour;
import com.example.travelagency.repository.TransportTourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransportTourService {

    private final TransportTourRepository transportTourRepository;

    @Autowired
    public TransportTourService(TransportTourRepository transportTourRepository) {
        this.transportTourRepository = transportTourRepository;
    }

    public List<TransportTour> getAllTransportTours() {
        return transportTourRepository.findAll();
    }
    public List<TransportTourDTO> getAllTransportObjects(){
        return transportTourRepository.findAllObjects();
    }
    public Optional<TransportTour> getTransportTourById(TransportTourId id) {
        return transportTourRepository.findById(id);
    }

    public TransportTour createTransportTour(TransportTour transportTour) {
        return transportTourRepository.save(transportTour);
    }

    public TransportTour updateTransportTour(TransportTourId id, TransportTour transportTourDetails) {
        transportTourDetails.setId(id);
        return transportTourRepository.save(transportTourDetails);
    }

    public void deleteTransportTour(TransportTourId id) {
        transportTourRepository.deleteById(id);
    }
}