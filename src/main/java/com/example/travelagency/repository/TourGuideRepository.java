package com.example.travelagency.repository;

import com.example.travelagency.Id.TourGuideId;
import com.example.travelagency.entity.TourGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourGuideRepository extends JpaRepository<TourGuide, TourGuideId> {
}