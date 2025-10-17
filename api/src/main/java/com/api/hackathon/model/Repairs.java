package com.api.hackathon.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "repairs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Repairs", description = "Repairs resource")

public class Repairs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Idreparation")

    private Integer idReparation;

    @Column(name = "status_babyfoot", length = 100)
    @Schema(description = "Statusbabyfoot")

    private String statusBabyfoot;

    @Column(name = "start_date_repairs", nullable = false)
    @Schema(description = "Startdaterepairs")

    private LocalDateTime startDateRepairs;

    @Column(name = "end_date_repairs", nullable = false)
    @Schema(description = "Enddaterepairs")

    private LocalDateTime endDateRepairs;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_babyfoot", nullable = false)
    @Schema(description = "Babyfoot")

    private Babyfoot babyfoot;
}