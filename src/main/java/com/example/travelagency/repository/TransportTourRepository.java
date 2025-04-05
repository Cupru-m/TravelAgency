package com.example.travelagency.repository;

import com.example.travelagency.Id.TransportTourId;
import com.example.travelagency.dtos.TransportTourDTO;
import com.example.travelagency.entity.TransportTour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface TransportTourRepository extends JpaRepository<TransportTour, TransportTourId> {

    @Query("SELECT new com.example.travelagency.dtos.TransportTourDTO(tt.id, tt.departureDate,tt.arrivalDate) FROM TransportTour tt")
    List<TransportTourDTO> findAllObjects();
}