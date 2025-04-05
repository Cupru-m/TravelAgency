package com.example.travelagency.service;

import com.example.travelagency.entity.Transport;
import com.example.travelagency.repository.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransportService {

    private final TransportRepository transportRepository;

    @Autowired
    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public List<Transport> getAllTransports() {
        return transportRepository.findAll();
    }

    public Optional<Transport> getTransportById(Integer id) {
        return transportRepository.findById(id);
    }

    public Transport createTransport(Transport transport) {
        return transportRepository.save(transport);
    }

    public Transport updateTransport(Integer id, Transport transportDetails) {
        transportDetails.setTransportId(id);
        return transportRepository.save(transportDetails);
    }

    public void deleteTransport(Integer id) {
        transportRepository.deleteById(id);
    }
}