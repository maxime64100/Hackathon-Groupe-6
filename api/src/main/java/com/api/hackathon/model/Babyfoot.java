package com.api.hackathon.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Schema(name = "Babyfoot", description = "Table de babyfoot")
@Entity
@Table(name = "babyfoot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Babyfoot {

    @Schema(description = "Identifiant du babyfoot", example = "3")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBabyfoot;

    @Schema(description = "Emplacement", example = "Souk - Zone A")
    @Column(length = 100)
    private String place;

    @Schema(description = "Statut", example = "free", allowableValues = {"free", "busy", "maintenance"})
    @Column(length = 50, nullable = false)
    private String statutBabyfoot = "free";

    @Schema(description = "Utilisable ?", example = "true")
    @Column(nullable = false)
    private Boolean usable = true;
}