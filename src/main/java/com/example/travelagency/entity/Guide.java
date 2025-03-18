package com.example.travelagency.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "guide")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Гид_id_seq")
    @SequenceGenerator(name = "Гид_id_seq", sequenceName = "Гид_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "experience", nullable = false)
    private String experience;

}