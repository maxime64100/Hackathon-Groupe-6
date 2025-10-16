package com.api.hackathon.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "repairs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repairs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReparation;

    @Column(name = "status_babyfoot", length = 100)
    private String statusBabyfoot;

    @Column(name = "start_date_repairs", nullable = false)
    private LocalDateTime startDateRepairs;

    @Column(name = "end_date_repairs", nullable = false)
    private LocalDateTime endDateRepairs;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_babyfoot", nullable = false)
    private Babyfoot babyfoot;
}