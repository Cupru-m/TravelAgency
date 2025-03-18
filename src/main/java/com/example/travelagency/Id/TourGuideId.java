package com.example.travelagency.Id;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class TourGuideId implements Serializable {
    private Long tourId;
    private Long guideId;

    public TourGuideId(Long tourId, Long guideId)
    {
        this.guideId = guideId;
        this.tourId = tourId;
    }

    public TourGuideId() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourGuideId)) return false;
        TourGuideId that = (TourGuideId) o;
        return Objects.equals(tourId, that.tourId) && Objects.equals(guideId, that.guideId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourId, guideId);
    }
}