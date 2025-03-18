package com.example.travelagency.entity;

import com.example.travelagency.Id.TourGuideId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tour_guide")
public class TourGuide {

    @EmbeddedId
    private TourGuideId id;

    @ManyToOne
    @MapsId("tourId")  // Связываем tourId из TourGuideId
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToOne
    @MapsId("guideId")  // Связываем guideId из TourGuideId
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;
}