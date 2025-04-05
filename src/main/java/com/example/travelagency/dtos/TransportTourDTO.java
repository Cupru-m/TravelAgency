package com.example.travelagency.dtos;

import com.example.travelagency.Id.TransportTourId;
import lombok.Data;

import java.time.LocalDate;
@Data
public class TransportTourDTO {
    private Long tourId;
    private Long transportId;
    private LocalDate departureDate;
    private LocalDate arrivalDate;

    // Конструктор
    public TransportTourDTO(TransportTourId id, LocalDate departureDate, LocalDate arrivalDate) {
        this.tourId =id.getTourId();
        this.transportId = id.getTransportId();
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }


}