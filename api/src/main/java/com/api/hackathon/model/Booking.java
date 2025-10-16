package com.api.hackathon.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBooking;

    @Column(nullable = false)
    private String statutBooking;

    @Column(nullable = false)
    private LocalDateTime dateBooking;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserBabyfoot user;

    @ManyToOne
    @JoinColumn(name = "id_babyfoot", nullable = false)
    private Babyfoot babyfoot;
}