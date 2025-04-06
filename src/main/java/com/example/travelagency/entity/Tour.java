package com.example.travelagency.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Тур_id_seq")
    @SequenceGenerator(name = "Тур_id_seq", sequenceName = "Тур_id_seq", allocationSize = 1)
    @Column(name = "tour_id")
    private Integer tourId;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price", nullable = false)
    private Long price;

}