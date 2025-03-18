package com.example.travelagency.repository;

import com.example.travelagency.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Integer> {

}