package com.api.hackathon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Schema(name = "Booking", description = "Réservation d’un babyfoot")
@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Schema(description = "Identifiant de réservation", example = "12")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBooking;

    @Schema(description = "Statut de la réservation", example = "confirmed", allowableValues = {"pending","confirmed","cancelled"})
    @Column(nullable = false)
    private String statutBooking;

    @Schema(description = "Date/heure de la réservation (ISO-8601)", example = "2025-10-16T18:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime dateBooking;

    @Schema(description = "Utilisateur ayant réservé")
    @ManyToOne @JoinColumn(name = "id_user", nullable = false)
    private UserBabyfoot user;

    @Schema(description = "Babyfoot réservé")
    @ManyToOne @JoinColumn(name = "id_babyfoot", nullable = false)
    private Babyfoot babyfoot;
}