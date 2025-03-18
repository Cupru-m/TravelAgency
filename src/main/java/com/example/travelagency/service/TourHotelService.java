package com.example.travelagency.service;

import com.example.travelagency.Id.TourHotelId;
import com.example.travelagency.entity.TourHotel;
import com.example.travelagency.repository.TourHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourHotelService {

    private final TourHotelRepository tourHotelRepository;

    @Autowired
    public TourHotelService(TourHotelRepository tourHotelRepository) {
        this.tourHotelRepository = tourHotelRepository;
    }

    public List<TourHotel> getAllTourHotels() {
        return tourHotelRepository.findAll();
    }

    public Optional<TourHotel> getTourHotelById(TourHotelId id) {
        return tourHotelRepository.findById(id);
    }

    public TourHotel createTourHotel(TourHotel tourHotel) {
        return tourHotelRepository.save(tourHotel);
    }

    public TourHotel updateTourHotel(TourHotelId id, TourHotel tourHotelDetails) {
        tourHotelDetails.setId(id);
        return tourHotelRepository.save(tourHotelDetails);
    }

    public void deleteTourHotel(TourHotelId id) {
        tourHotelRepository.deleteById(id);
    }
}