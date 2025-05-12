package com.sh32bit.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "salons")
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ElementCollection
    private List<String> images;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private boolean isOpen;

    @Column(nullable = false)
    private boolean homeService;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;
}
