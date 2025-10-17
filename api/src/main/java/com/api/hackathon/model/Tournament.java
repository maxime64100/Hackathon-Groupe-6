package com.api.hackathon.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "tournament")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Tournament", description = "Tournament resource")

public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Idtournament")

    private Integer idTournament;

    @Column(name = "date_tournament", nullable = false)
    @Schema(description = "Datetournament")

    private LocalDateTime dateTournament;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @Schema(description = "User")

    private UserBabyfoot user; // nullable by schema (ON DELETE SET NULL)
}