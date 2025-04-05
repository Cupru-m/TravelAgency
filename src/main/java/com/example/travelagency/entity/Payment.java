package com.example.travelagency.entity;



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Оплата_id_seq")
    @SequenceGenerator(name = "Оплата_id_seq", sequenceName = "Оплата_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name="booking_id")
    private Integer booking_id;
}