package com.example.travelagency.repository;

import com.example.travelagency.Id.TourHotelId;
import com.example.travelagency.entity.TourHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface TourHotelRepository extends JpaRepository<TourHotel, TourHotelId> {


    @Query("SELECT th.id FROM TourHotel th")
    List<TourHotelId> findAllId();
}