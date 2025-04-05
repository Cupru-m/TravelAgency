package com.example.travelagency.service;

import com.example.travelagency.Id.TourGuideId;
import com.example.travelagency.entity.TourGuide;
import com.example.travelagency.repository.TourGuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourGuideService {

    private final TourGuideRepository tourGuideRepository;

    @Autowired
    public TourGuideService(TourGuideRepository tourGuideRepository) {
        this.tourGuideRepository = tourGuideRepository;
    }

    public List<TourGuide> getAllTourGuides() {
        return tourGuideRepository.findAll();
    }
    public List<TourGuideId> getAllTourGiudeId()
    {
        return tourGuideRepository.findAllId();
    }

    public Optional<TourGuide> getTourGuideById(TourGuideId id) {
        return tourGuideRepository.findById(id);
    }

    public TourGuide createTourGuide(TourGuide tourGuide) {
        return tourGuideRepository.save(tourGuide);
    }

    public TourGuide updateTourGuide(TourGuideId id, TourGuide tourGuideDetails) {
        tourGuideDetails.setTour(tourGuideRepository.findById(id).orElseThrow().getTour());
        return tourGuideRepository.save(tourGuideDetails);
    }

    public void deleteTourGuide(TourGuideId id) {
        tourGuideRepository.deleteById(id);
    }
}
