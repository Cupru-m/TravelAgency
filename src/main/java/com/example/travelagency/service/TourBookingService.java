package com.example.travelagency.service;

import com.example.travelagency.entity.TourBooking;
import com.example.travelagency.repository.TourBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.travelagency.Id.TourBookingId;
import java.util.List;
import java.util.Optional;

@Service
public class TourBookingService {

    private final TourBookingRepository tourBookingRepository;

    @Autowired
    public TourBookingService(TourBookingRepository tourBookingRepository) {
        this.tourBookingRepository = tourBookingRepository;
    }

    public List<TourBooking> getAllTourBookings() {
        return tourBookingRepository.findAll();
    }

    public Optional<TourBooking> getTourBookingById(TourBookingId id) {
        return tourBookingRepository.findById(id);
    }

    public TourBooking createTourBooking(TourBooking tourBooking) {
        return tourBookingRepository.save(tourBooking);
    }

    public TourBooking updateTourBooking(TourBookingId id, TourBooking tourBookingDetails) {
        tourBookingDetails.setId(id);
        return tourBookingRepository.save(tourBookingDetails);
    }

    public void deleteTourBooking(TourBookingId id) {
        tourBookingRepository.deleteById(id);
    }
}