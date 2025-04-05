package com.example.travelagency.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Отель_id_seq")
    @SequenceGenerator(name = "Отель_id_seq", sequenceName = "Отель_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "phone_number", nullable = false, unique = true)
    private Long phoneNumber;

    @Column(name = "price", nullable = false)
    private int price;

    // Getters and Setters
    // ...
}