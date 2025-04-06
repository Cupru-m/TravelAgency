package com.example.travelagency.service;

import com.example.travelagency.entity.Tour;
import com.example.travelagency.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Optional<Tour> getTourById(Integer id) {
        return tourRepository.findById(id);
    }

    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public Tour updateTour(Integer id, Tour tourDetails) {
        tourDetails.setTourId(id);
        return tourRepository.save(tourDetails);
    }

    public void deleteTour(Integer id) {
        tourRepository.deleteById(id);
    }
}