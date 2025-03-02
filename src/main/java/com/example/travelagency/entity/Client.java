package com.example.travelagency.entity;

import jakarta.persistence.*;
import jdk.jfr.Relational;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
@Getter
@Setter
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Уникальный идентификатор клиента.

    @Column(name = "first_name", nullable = false)
    private String firstName; // Имя клиента.

    @Column(name = "last_name", nullable = false)
    private String lastName; // Фамилия клиента.

    @Column(name = "middle_name", nullable = false)
    private String middleName; // Отчество клиента.

    @Column(name = "phone", nullable = false)
    private String phone; // Контактный номер телефона клиента.

    @Column(name = "email", nullable = false)
    private String email; // Электронная почта клиента.

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate; // Дата рождения клиента.

}