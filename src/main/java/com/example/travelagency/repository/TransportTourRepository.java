package com.example.travelagency.repository;

import com.example.travelagency.Id.TransportTourId;
import com.example.travelagency.entity.TransportTour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportTourRepository extends JpaRepository<TransportTour, TransportTourId> {
    // Дополнительные методы для поиска можно добавить здесь
}