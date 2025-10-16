package com.api.hackathon.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "babyfoot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Babyfoot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBabyfoot;

    @Column(length = 100)
    private String place;

    @Column(length = 50, nullable = false)
    private String statutBabyfoot = "free";

    @Column(nullable = false)
    private Boolean usable = true;
}