package com.example.travelagency.entity;

import com.example.travelagency.Id.TourGuideId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tour_guide")
public class TourGuide {

    @EmbeddedId
    private TourGuideId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tourId")  // Связываем tourId из TourGuideId
    @JoinColumn(name = "tour_id", nullable = false)
    @JsonIgnore
    private Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("guideId")  // Связываем guideId из TourGuideId
    @JoinColumn(name = "guide_id", nullable = false)
    @JsonIgnore
    private Guide guide;
}