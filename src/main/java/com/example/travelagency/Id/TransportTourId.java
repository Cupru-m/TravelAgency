package com.example.travelagency.Id;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class TransportTourId implements Serializable {

    private Long tourId;
    private Long transportId;

    public TransportTourId() {}

    public TransportTourId(Long tourId, Long transportId) {
        this.tourId = tourId;
        this.transportId = transportId;
    }

}
