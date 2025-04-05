package com.example.travelagency.repository;

import com.example.travelagency.Id.TourGuideId;
import com.example.travelagency.entity.TourGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface TourGuideRepository extends JpaRepository<TourGuide, TourGuideId> {

    @Query("SELECT tg.id FROM TourGuide tg")
    List<TourGuideId> findAllId();
}