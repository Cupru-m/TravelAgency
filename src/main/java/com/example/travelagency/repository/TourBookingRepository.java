package com.example.travelagency.repository;

import com.example.travelagency.Id.TourBookingId;
import com.example.travelagency.entity.TourBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourBookingRepository extends JpaRepository<TourBooking, TourBookingId> {
}