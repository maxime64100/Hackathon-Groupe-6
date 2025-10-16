package com.api.hackathon.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tournament")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTournament;

    @Column(name = "date_tournament", nullable = false)
    private LocalDateTime dateTournament;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserBabyfoot user; // nullable by schema (ON DELETE SET NULL)
}