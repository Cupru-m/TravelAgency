package com.example.travelagency.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transport")
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Транспорт_id_seq")
    @SequenceGenerator(name = "Транспорт_id_seq", sequenceName = "Транспорт_id_seq", allocationSize = 1)
    @Column(name = "transport_id")
    private Integer transportId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    // Getters and Setters
    // ...
}
