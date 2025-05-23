package com.example.travelagency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Бронирование_id_seq")
    @SequenceGenerator(name = "Бронирование_id_seq", sequenceName = "Бронирование_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "adult_places_count")
    private Long adultPlacesCount;

    @Column(name = "children_places_count")
    private Long childrenPlacesCount;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name= "tour_id", nullable = false)
    private Long tourId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore // игнорирует поле при сериализации
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore // игнорирует поле при сериализации
    @JoinColumn(name = "client_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Client client;


}