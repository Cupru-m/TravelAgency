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
    @Column(name = "id_транспорта")
    private Integer id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "capacity", nullable = false)
    private String capacity;

    // Getters and Setters
    // ...
}
