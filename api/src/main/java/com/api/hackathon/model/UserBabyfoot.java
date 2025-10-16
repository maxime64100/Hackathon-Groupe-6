package com.api.hackathon.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Schema(name = "User", description = "Utilisateur de l’application Babyfoot")
@Entity
@Table(name = "user_babyfoot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBabyfoot {
    @Schema(description = "Identifiant utilisateur", example = "1")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Schema(description = "Prénom", example = "Raphaël")
    private String name;

    @Schema(description = "Nom", example = "Queron")
    private String surname;

    @Schema(description = "Adresse email unique", example = "raphael.queron@ynov.com")
    private String mail;

    @Schema(description = "Mot de passe (hashé côté API)", example = "$2a$10$...")
    private String passwordUser;

    @Schema(description = "Rôle applicatif", example = "USER")
    private String role = "USER";

    @Schema(description = "Rôle en jeu (optionnel)", example = "attaquant")
    private String roleGame;
}